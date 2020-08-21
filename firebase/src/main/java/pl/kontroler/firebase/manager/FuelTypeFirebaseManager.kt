package pl.kontroler.firebase.manager

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.kontroler.firebase.model.FuelTypeFirebase
import pl.kontroler.firebase.util.Resource


/**
 * @author Rafał Nowowieski
 */

class FuelTypeFirebaseManager(
    private val firestore: FirebaseFirestore
) {

    suspend fun all(): Resource<List<FuelTypeFirebase>> {
        val returnedList = firestore
            .collection("fuelTypes")
            .get()
            .await()
            .map { document -> document.toObject(FuelTypeFirebase::class.java) }
        return Resource.Success(returnedList)
    }

}