package pl.kontroler.carspendsmoney

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinComponent
import org.koin.dsl.module
import pl.kontroler.carspendsmoney.ui.login.LoginViewModel


/**
 * @author Rafał Nowowieski
 */

class AppModule : KoinComponent {

    val appModule = module {
        viewModel { LoginViewModel() }
    }

}