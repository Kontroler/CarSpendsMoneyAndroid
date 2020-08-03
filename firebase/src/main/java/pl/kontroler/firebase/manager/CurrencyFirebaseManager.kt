package pl.kontroler.firebase.manager

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.kontroler.firebase.model.CurrencyFirebase
import pl.kontroler.firebase.util.Resource2


/**
 * @author Rafał Nowowieski
 */

class CurrencyFirebaseManager(
    private val firestore: FirebaseFirestore
) {

    suspend fun readAll(): Resource2<List<CurrencyFirebase>> {
        val returnedList = firestore
            .collection("currencies")
            .get()
            .await()
            .map { document -> document.toObject(CurrencyFirebase::class.java) }
        return Resource2.Success(returnedList)
    }

}