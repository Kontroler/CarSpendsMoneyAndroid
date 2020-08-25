package pl.kontroler.firebase.manager

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import pl.kontroler.firebase.model.FirebaseUserLiveData
import timber.log.Timber


/**
 * @author Rafał Nowowieski
 */

class AuthenticationFirebaseManager(
    private val auth: FirebaseAuth
) {

    val authState: LiveData<FirebaseUser> = FirebaseUserLiveData(auth)
    val currentUser = auth.currentUser

    fun isUserLogged(): Boolean {
        return auth.currentUser != null
    }

    suspend fun createAccount(email: String, password: String, displayName: String): FirebaseUser {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            Timber.w("createUserWithEmailAndPassword:success")

            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            authResult.user?.updateProfile(profileUpdate)?.await()
            Timber.w("updateProfile:success")

            return auth.currentUser!!
        } catch (ex: Exception) {
            Timber.e("createUserWithEmailAndPassword:failure $ex")
            throw ex
        }
    }

    suspend fun logInWithEmail(email: String, password: String): FirebaseUser {
        try {
            val data = auth.signInWithEmailAndPassword(email, password).await()
            Timber.w("signInWithEmailAndPassword:success")
            return data.user!!
        } catch (ex: Exception) {
            Timber.e("signInWithEmailAndPassword:failure $ex")
            throw ex
        }
    }

}