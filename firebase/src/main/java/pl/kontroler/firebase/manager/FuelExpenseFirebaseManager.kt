package pl.kontroler.firebase.manager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.tasks.await
import pl.kontroler.firebase.model.FuelExpenseFirebase
import pl.kontroler.firebase.util.IdValuePair
import pl.kontroler.firebase.util.Resource2
import timber.log.Timber


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
class FuelExpenseFirebaseManager(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val carFirebaseManager: CarFirebaseManager
) {

    suspend fun write(fuelExpense: FuelExpenseFirebase) {
        carFirebaseManager.getCurrentCar().collect {
            firestore
                .collection("users")
                .document(auth.currentUser!!.uid)
                .collection("car")
                .document((it as Resource2.Success).data!!.id)
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
    }

    /**
     *
     *  @return list of uid: String and fuelExpenseFirebase: FuelExpenseFirebase
     */
    suspend fun readAll(): Resource2<List<IdValuePair<FuelExpenseFirebase>>> {
        val returnedList = firestore
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("car")
            .document("expense")
            .collection("fuelExpense")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()
            .map { document ->
                val id = document.id
                val fuelExpenseFirebase =
                    document.toObject(FuelExpenseFirebase::class.java)
                IdValuePair(id, fuelExpenseFirebase)
            }

        return Resource2.Success(returnedList)
    }

    suspend fun readAllCoroutines(): Flow<Resource2<List<IdValuePair<FuelExpenseFirebase>>>> {
        return callbackFlow {
            val document = firestore
                .collection("users")
                .document(auth.currentUser!!.uid)
                .collection("car")
                .document("expense")
                .collection("fuelExpense")

            val subscription = document.addSnapshotListener { value, error ->
                val returnedList = mutableListOf<IdValuePair<FuelExpenseFirebase>>()
                try {
                    value?.forEach {
                        if (it.exists()) {
                            val id = it.id
                            val fuelExpenseFirebase = it.toObject(FuelExpenseFirebase::class.java)
                            val fuelExpense = IdValuePair(id, fuelExpenseFirebase)
                            returnedList.add(fuelExpense)
                        }
                    }
                    offer(Resource2.Success(returnedList))
                } catch (e: Exception) {
                    offer(Resource2.Failure(e))
                }
            }
            awaitClose { subscription.remove() }
        }
    }
}
