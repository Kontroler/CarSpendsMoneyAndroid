package pl.kontroler.domain.manager

import com.google.firebase.auth.AuthResult
import pl.kontroler.domain.mapper.UserMapper
import pl.kontroler.domain.model.User
import pl.kontroler.firebase.manager.AuthenticationFirebaseManager


/**
 * @author Rafał Nowowieski
 */

class AuthenticationDomainManager(
    private val authenticationFirebaseManager: AuthenticationFirebaseManager,
    private val userMapper: UserMapper
) {
    val authState = authenticationFirebaseManager.authState

    fun isUserLogged(): Boolean {
        return authenticationFirebaseManager.isUserLogged()
    }

    fun getUser(): User {
        return userMapper.mapToModel(authenticationFirebaseManager.currentUser)
    }

    suspend fun createAccount(email: String, password: String, displayName: String): User {
        val user = authenticationFirebaseManager.createAccount(email, password, displayName)
        return userMapper.mapToModel(user)
    }

    suspend fun logInWithEmail(email: String, password: String): User {
        val user = authenticationFirebaseManager.logInWithEmail(email, password)
        return userMapper.mapToModel(user)
    }

}