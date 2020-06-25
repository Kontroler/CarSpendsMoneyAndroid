package pl.kontroler.carspendsmoney

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import pl.kontroler.carspendsmoney.ui.MainViewModel
import pl.kontroler.carspendsmoney.ui.home.HomeViewModel
import pl.kontroler.carspendsmoney.ui.login.LoginViewModel
import pl.kontroler.carspendsmoney.ui.register.RegisterViewModel
import pl.kontroler.domain.DomainModule


/**
 * @author Rafał Nowowieski
 */

class AppModule : KoinComponent {

    init {
        loadKoinModules(DomainModule().domainModule)
    }

    val appModule = module {
        viewModel { MainViewModel(get()) }
        viewModel { LoginViewModel(get()) }
        viewModel { RegisterViewModel(get()) }
        viewModel { HomeViewModel(get()) }
    }

}