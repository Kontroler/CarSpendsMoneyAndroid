package pl.kontroler.firebase.manager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import pl.kontroler.firebase.model.CarFirebase
import pl.kontroler.firebase.util.IdValuePair
import pl.kontroler.firebase.util.Resource2
import timber.log.Timber


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
class CarFirebaseManager(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    suspend fun getCurrentCar(): Flow<Resource2<IdValuePair<CarFirebase>?>> {
        return callbackFlow {
            val document = firestore
                .collection("users")
                .document(auth.currentUser!!.uid)
                .collection("car")
                .whereEqualTo("isCurrent", true)
                .limit(1)

            val subscription = document.addSnapshotListener { value, error ->
                try {
                    value?.forEach {
                        if (it.exists()) {
                            val id = it.id
                            val fuelExpenseFirebase = it.toObject(CarFirebase::class.java)
                            val returnedCar = IdValuePair(id, fuelExpenseFirebase)
                            offer(Resource2.Success(returnedCar))
                        }
                    }
                } catch (e: Exception) {
                    offer(Resource2.Failure(e))
                }
            }

            awaitClose { subscription.remove() }
        }
    }

    suspend fun getAll(): Flow<Resource2<List<IdValuePair<CarFirebase>>>> {
        return callbackFlow {
            val document = firestore
                .collection("users")
                .document(auth.currentUser!!.uid)
                .collection("car")
                .whereEqualTo("isCurrent", true)
                .limit(1)

            val subscription = document.addSnapshotListener { value, error ->
                try {
                    val returnedCars = mutableListOf<IdValuePair<CarFirebase>>()
                    value?.forEach {
                        if (it.exists()) {
                            val id = it.id
                            val fuelExpenseFirebase = it.toObject(CarFirebase::class.java)
                            val returnedCar = IdValuePair(id, fuelExpenseFirebase)
                            returnedCars.add(returnedCar)
                        }
                    }
                    offer(Resource2.Success(returnedCars))
                } catch (e: Exception) {
                    offer(Resource2.Failure(e))
                }
            }

            awaitClose { subscription.remove() }
        }
    }

    suspend fun write(carFirebase: CarFirebase) {
        firestore
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("car")
            .add(carFirebase)
            .addOnSuccessListener {
                Timber.d("DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Timber.w("Error writing document: $e")
            }
            .await()
    }

    suspend fun setCurrentCar(uid: String) {
        firestore
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("car")
            .document(uid)
            .update("isCurrent", true)
            .addOnSuccessListener {
                Timber.d("DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Timber.w("Error writing document: $e")
            }
            .await()
    }

}