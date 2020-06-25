package pl.kontroler.carspendsmoney.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.kontroler.domain.manager.AuthenticationDomainManager

class LoginViewModel(
    private val authenticationDomainManager: AuthenticationDomainManager
) : ViewModel() {

    enum class AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,        // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }

    val authenticationState = MutableLiveData<AuthenticationState>()

    init {
        viewModelScope.launch {
            if (authenticationDomainManager.isUserLogged()) {
                authenticationState.value = AuthenticationState.AUTHENTICATED
            } else {
                authenticationState.value = AuthenticationState.UNAUTHENTICATED
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                authenticationDomainManager.logInWithEmail(email, password)
                authenticationState.value = AuthenticationState.AUTHENTICATED
            } catch (ex: Exception) {
                authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
            }
        }
    }

    fun refuseAuthentication() {
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

}