package pl.kontroler.firebase.manager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import pl.kontroler.firebase.model.CarFirebase
import pl.kontroler.firebase.util.IdValuePair
import pl.kontroler.firebase.util.Resource
import timber.log.Timber


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
class CarFirebaseManager(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    suspend fun currentCar(): IdValuePair<CarFirebase>? {
        val queryDocumentSnapshot = firestore
            .collection("users")
            .document(auth.currentUser!!.uid)
            .collection("car")
            .whereEqualTo("current", true)
            .limit(1)
            .get()
            .await()
            .first { it.exists() }
            ?: return null

        return makeCar(queryDocumentSnapshot)
    }

    suspend fun currentCarFlow(): Flow<Resource<IdValuePair<CarFirebase>?>> {
        return callbackFlow {
            val document = firestore
                .collection("users")
                .document(auth.currentUser!!.uid)
                .collection("car")
                .whereEqualTo("current", true)
                .limit(1)

            val subscription = document.addSnapshotListener { value, error ->
                try {
                    value?.forEach {
                        if (it.exists()) {
                            offer(Resource.Success(makeCar(it)))
                        }
                    }
                } catch (e: Exception) {
                    offer(Resource.Failure(e)) as Unit
                }
            }

            awaitClose { subscription.remove() }
        }
    }

    suspend fun allFlow(): Flow<Resource<List<IdValuePair<CarFirebase>>>> {
        return callbackFlow {
            val document = firestore
                .collection("users")
                .document(auth.currentUser!!.uid)
                .collection("car")

            val subscription = document.addSnapshotListener { value, error ->
                try {
                    val returnedCars = mutableListOf<IdValuePair<CarFirebase>>()
                    value?.forEach {
                        if (it.exists()) {
                            returnedCars.add(makeCar(it))
                        }
                    }
                    offer(Resource.Success(returnedCars))
                } catch (e: Exception) {
                    offer(Resource.Failure(e)) as Unit
                }
            }

            awaitClose { subscription.remove() }
        }
    }

    private fun makeCar(it: QueryDocumentSnapshot): IdValuePair<CarFirebase> {
        val id = it.id
        val name = it.getString("name")
        val counter = it.getLong("counter")
        val isCurrent = it.getBoolean("current")
        val carFirebase = CarFirebase(
            name = name,
            counter = counter?.toInt(),
            isCurrent = isCurrent
        )
        return IdValuePair(id, carFirebase)
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
            .get()
            .addOnSuccessListener {
                it.documents.forEach { document ->
                    if (document.id == uid) {
                        document.reference.update("current", true)
                    } else {
                        document.reference.update("current", false)
                    }
                }
                Timber.d("DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Timber.w("Error writing document: $e")
            }
            .await()
    }

}