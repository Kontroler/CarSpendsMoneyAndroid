package pl.kontroler.firebase.manager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import pl.kontroler.firebase.NextFuelExpenseCounterIsLowerException
import pl.kontroler.firebase.PreviousFuelExpenseCounterIsGreaterException
import pl.kontroler.firebase.model.FuelExpenseFirebase
import pl.kontroler.firebase.util.IdValuePair
import pl.kontroler.firebase.util.Resource
import timber.log.Timber


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
class FuelExpenseFirebaseManager(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    suspend fun write(fuelExpense: FuelExpenseFirebase, carUid: String) {
        val checkFuelExpenses = firestore
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("car")
            .document(carUid)
            .collection("fuelExpense")
            .orderBy("date")
            .orderBy("counter")
            .get()
            .await()

        val previousFuelExpense = checkFuelExpenses?.documents?.findLast { document ->
            val date = document.getTimestamp("date")
            date!! < fuelExpense.date!!
        }
        val previousCounter = previousFuelExpense?.getLong("counter")
        val previousDate = previousFuelExpense?.getTimestamp("date")?.toDate()
        if (previousFuelExpense != null && previousCounter!! > fuelExpense.counter!!) {
            throw PreviousFuelExpenseCounterIsGreaterException(
                "Previous counter: $previousCounter - $previousDate, " +
                        "New counter: ${fuelExpense.counter} - ${fuelExpense.date?.toDate()}"
            )
        }

        val nextFuelExpense = checkFuelExpenses?.documents?.firstOrNull() { document ->
            val date = document.getTimestamp("date")
            date!! > fuelExpense.date!!
        }
        val nextCounter = nextFuelExpense?.getLong("counter")
        val nextDate = nextFuelExpense?.getTimestamp("date")?.toDate()
        if (nextFuelExpense != null && nextCounter!! < fuelExpense.counter!!) {
            throw NextFuelExpenseCounterIsLowerException(
                "Next counter: $nextCounter - $nextDate, " +
                        "New counter: ${fuelExpense.counter} - ${fuelExpense.date?.toDate()}"
            )
        }

        firestore
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("car")
            .document(carUid)
            .collection("fuelExpense")
            .add(fuelExpense)
            .addOnSuccessListener {
                Timber.d("DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Timber.w("Error writing document: $e")
            }
            .await()
    }

    suspend fun allFlow(
        carUid: String,
        queryDirection: Query.Direction
    ): Flow<Resource<List<IdValuePair<FuelExpenseFirebase>>>> {
        return callbackFlow {
            val document = firestore
                .collection("users")
                .document(auth.currentUser!!.uid)
                .collection("car")
                .document(carUid)
                .collection("fuelExpense")
                .orderBy("date", queryDirection)
                .orderBy("counter", queryDirection)

            val subscription = document.addSnapshotListener { value, error ->
                try {
                    val returnedList = mutableListOf<IdValuePair<FuelExpenseFirebase>>()
                    value?.forEach {
                        if (it.exists()) {
                            val id = it.id
                            val fuelExpenseFirebase =
                                it.toObject(FuelExpenseFirebase::class.java)
                            val fuelExpense = IdValuePair(id, fuelExpenseFirebase)
                            returnedList.add(fuelExpense)
                        }
                    }
                    offer(Resource.Success(returnedList))
                } catch (e: Exception) {
                    offer(Resource.Failure(e)) as Unit
                }
            }
            awaitClose { subscription.remove() }
        }
    }

}
