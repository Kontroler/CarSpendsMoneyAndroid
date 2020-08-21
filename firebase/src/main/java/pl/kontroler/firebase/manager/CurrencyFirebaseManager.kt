package pl.kontroler.firebase.manager

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.kontroler.firebase.model.CurrencyFirebase
import pl.kontroler.firebase.util.Resource


/**
 * @author Rafał Nowowieski
 */

class CurrencyFirebaseManager(
    private val firestore: FirebaseFirestore
) {

    suspend fun all(): Resource<List<CurrencyFirebase>> {
        val returnedList = firestore
            .collection("currencies")
            .get()
            .await()
            .map { document -> document.toObject(CurrencyFirebase::class.java) }
        return Resource.Success(returnedList)
    }

}