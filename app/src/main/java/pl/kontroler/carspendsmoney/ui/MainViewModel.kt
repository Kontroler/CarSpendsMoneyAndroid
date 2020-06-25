package pl.kontroler.carspendsmoney.ui

import androidx.lifecycle.ViewModel
import pl.kontroler.domain.manager.AuthenticationDomainManager


/**
 * @author Rafał Nowowieski
 */

class MainViewModel(
    private val authenticationDomainManager: AuthenticationDomainManager
) : ViewModel() {

    val authState = authenticationDomainManager.authState

}