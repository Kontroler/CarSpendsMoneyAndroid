package pl.kontroler.firebase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module
import pl.kontroler.firebase.manager.AuthenticationFirebaseManager
import pl.kontroler.firebase.manager.RealtimeDatabaseFirebaseManager


/**
 * @author Rafał Nowowieski
 */

val firebaseModule = module {

    single { AuthenticationFirebaseManager(get()) }
    single { RealtimeDatabaseFirebaseManager(get()) }

    single { Firebase.auth }
    single { FirebaseDatabase.getInstance().reference }
}