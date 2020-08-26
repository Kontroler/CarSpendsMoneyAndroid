package pl.kontroler.firebase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module
import pl.kontroler.firebase.manager.*


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
val firebaseModule = module {

    single { AuthenticationFirebaseManager(get()) }
    single { FuelExpenseFirebaseManager(get(), get()) }
    single { CurrencyFirebaseManager(get()) }
    single { FuelTypeFirebaseManager(get()) }
    single { CarFirebaseManager(get(), get()) }
    single { ServiceExpenseFirebaseManager(get(), get()) }

    single { Firebase.auth }
    single { FirebaseDatabase.getInstance().reference }
    single { Firebase.firestore }
}