package pl.kontroler.firebase.manager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import pl.kontroler.firebase.DeleteFuelExpenseException
import pl.kontroler.firebase.NextServiceExpenseCounterIsLowerException
import pl.kontroler.firebase.PreviousServiceExpenseCounterIsGreaterException
import pl.kontroler.firebase.WriteServiceExpenseException
import pl.kontroler.firebase.model.ServiceExpenseFirebase
import pl.kontroler.firebase.util.IdValuePair
import pl.kontroler.firebase.util.Resource
import timber.log.Timber


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
class ServiceExpenseFirebaseManager(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {

    suspend fun write(
        serviceExpenseFirebase: ServiceExpenseFirebase,
        carUid: String,
    ): Resource<Unit> {
        checkServiceExpenses(carUid, serviceExpenseFirebase)

        var result: Resource<Unit> =
            Resource.Failure(WriteServiceExpenseException("Default value did not changed"))
        firestore
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("car")
            .document(carUid)
            .collection("serviceExpense")
            .add(serviceExpenseFirebase)
            .addOnSuccessListener {
                Timber.d("DocumentSnapshot successfully written!")
                result = Resource.Success(Unit)
            }
            .addOnFailureListener { e ->
                Timber.w("Error writing document: $e")
                result = Resource.Failure(e)
            }
            .await()
        return result
    }

    private suspend fun checkServiceExpenses(
        carUid: String,
        serviceExpenseFirebase: ServiceExpenseFirebase,
    ) {
        val checkServiceExpenses = firestore
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("car")
            .document(carUid)
            .collection("serviceExpense")
            .orderBy("date")
            .orderBy("counter")
            .get()
            .await()

        if (checkServiceExpenses != null) {
            checkPreviousServiceExpense(checkServiceExpenses, serviceExpenseFirebase)
            checkNextServiceExpense(checkServiceExpenses, serviceExpenseFirebase)
        }
    }

    private fun checkNextServiceExpense(
        checkServiceExpenses: QuerySnapshot,
        serviceExpenseFirebase: ServiceExpenseFirebase,
    ) {
        val nextServiceExpense = checkServiceExpenses.documents.firstOrNull() { document ->
            val date = document.getTimestamp("date")
            date!! > serviceExpenseFirebase.date!!
        }
        val nextCounter = nextServiceExpense?.getLong("counter")
        val nextDate = nextServiceExpense?.getTimestamp("date")?.toDate()
        if (nextServiceExpense != null && nextCounter!! < serviceExpenseFirebase.counter!!) {
            throw NextServiceExpenseCounterIsLowerException(
                "Next counter: $nextCounter - $nextDate, " +
                        "New counter: ${serviceExpenseFirebase.counter} - ${serviceExpenseFirebase.date?.toDate()}"
            )
        }
    }

    private fun checkPreviousServiceExpense(
        checkServiceExpenses: QuerySnapshot,
        serviceExpenseFirebase: ServiceExpenseFirebase,
    ) {
        val previousServiceExpense = checkServiceExpenses.documents.findLast { document ->
            val date = document.getTimestamp("date")
            date!! < serviceExpenseFirebase.date!!
        }
        val previousCounter = previousServiceExpense?.getLong("counter")
        val previousDate = previousServiceExpense?.getTimestamp("date")?.toDate()
        if (previousServiceExpense != null && previousCounter!! > serviceExpenseFirebase.counter!!) {
            throw PreviousServiceExpenseCounterIsGreaterException(
                "Previous counter: $previousCounter - $previousDate, " +
                        "New counter: ${serviceExpenseFirebase.counter} - ${serviceExpenseFirebase.date?.toDate()}"
            )
        }
    }

    suspend fun allFlow(
        carUid: String,
        queryDirection: Query.Direction,
    ): Flow<Resource<List<IdValuePair<ServiceExpenseFirebase>>>> {
        return callbackFlow {
            val document = firestore
                .collection("users")
                .document(auth.currentUser!!.uid)
                .collection("car")
                .document(carUid)
                .collection("serviceExpense")
                .orderBy("date", queryDirection)
                .orderBy("counter", queryDirection)

            val subscription = document.addSnapshotListener { value, error ->
                try {
                    val returnedList = makeIdValuePairList(value)
                    offer(Resource.Success(returnedList))
                } catch (e: Exception) {
                    offer(Resource.Failure(e)) as Unit
                }
            }
            awaitClose { subscription.remove() }
        }
    }

    private fun makeIdValuePairList(value: QuerySnapshot?): List<IdValuePair<ServiceExpenseFirebase>> {
        val returnedList = mutableListOf<IdValuePair<ServiceExpenseFirebase>>()
        value?.forEach {
            if (it.exists()) {
                val serviceExpenseIdValuePair = makeIdValuePair(it)
                returnedList.add(serviceExpenseIdValuePair)
            }
        }
        return returnedList
    }

    private fun makeIdValuePair(it: QueryDocumentSnapshot): IdValuePair<ServiceExpenseFirebase> {
        val id = it.id
        val serviceExpenseFirebase = it.toObject(ServiceExpenseFirebase::class.java)
        return IdValuePair(id, serviceExpenseFirebase)
    }

    suspend fun delete(serviceExpenseUid: String, carUid: String): Resource<Unit> {
        var result: Resource<Unit> =
            Resource.Failure(DeleteFuelExpenseException("Default value did not changed"))
        firestore
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("car")
            .document(carUid)
            .collection("serviceExpense")
            .document(serviceExpenseUid)
            .delete()
            .addOnSuccessListener {
                result = Resource.Success(Unit)
            }
            .addOnFailureListener {
                result = Resource.Failure(it)
            }
            .await()
        return result
    }

}