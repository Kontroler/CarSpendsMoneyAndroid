package pl.kontroler.firebase.model

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


/**
 * @author Rafał Nowowieski
 */

class FirebaseUserLiveData(private val firebaseAuth: FirebaseAuth) : LiveData<FirebaseUser>() {

    private val authStateListener = FirebaseAuth.AuthStateListener {
        val firebaseUser = firebaseAuth.currentUser
        value = firebaseUser
    }

    override fun onActive() {
        super.onActive()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onInactive() {
        super.onInactive()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

}