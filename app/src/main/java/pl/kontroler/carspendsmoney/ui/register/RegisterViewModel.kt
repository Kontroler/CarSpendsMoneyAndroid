package pl.kontroler.carspendsmoney.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.launch
import pl.kontroler.carspendsmoney.R
import pl.kontroler.carspendsmoney.utils.SingleLiveEvent
import pl.kontroler.domain.manager.AuthenticationDomainManager
import pl.kontroler.domain.model.MessageResource
import pl.kontroler.domain.model.User
import java.lang.Exception

class RegisterViewModel(
    private val authenticationDomainManager: AuthenticationDomainManager
) : ViewModel() {

    var _password = MutableLiveData<String>().apply { value = "" }
    val password: LiveData<String> = _password

    private var _userCreated = SingleLiveEvent<User>()
    val userCreated: LiveData<User> = _userCreated

    private var _emailErrorResId = MutableLiveData<Int>()
    val emailErrorResId: LiveData<Int> = _emailErrorResId

    private var _passwordErrorResId = MutableLiveData<Int>()
    val passwordErrorResId: LiveData<Int> = _passwordErrorResId

    private var _messageResource = SingleLiveEvent<MessageResource>()
    val messageResource: LiveData<MessageResource> = _messageResource

    fun register(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            try {
                val user = authenticationDomainManager.createAccount(email, password, displayName)
                _messageResource.value = MessageResource(
                    MessageResource.Type.Success,
                    R.string.register_registerSuccessful
                )
                _userCreated.value = user
            } catch (e: FirebaseAuthUserCollisionException) {
                _emailErrorResId.value = R.string.register_userCollisionError
            } catch (e: FirebaseAuthWeakPasswordException) {
                _passwordErrorResId.value = R.string.register_weakPasswordError
            } catch (ex: Exception) {
                _messageResource.value = MessageResource(
                    MessageResource.Type.Error,
                    R.string.register_userCreatedError
                )
            }
        }
    }

}