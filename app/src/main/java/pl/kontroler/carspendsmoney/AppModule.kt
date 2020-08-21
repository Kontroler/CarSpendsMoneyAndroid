package pl.kontroler.carspendsmoney

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import pl.kontroler.carspendsmoney.ui.MainViewModel
import pl.kontroler.carspendsmoney.ui.editCar.EditCarDialogViewModel
import pl.kontroler.carspendsmoney.ui.home.HomeViewModel
import pl.kontroler.carspendsmoney.ui.login.LoginViewModel
import pl.kontroler.carspendsmoney.ui.newRefuel.NewRefuelViewModel
import pl.kontroler.carspendsmoney.ui.refuels.RefuelsViewModel
import pl.kontroler.carspendsmoney.ui.register.RegisterViewModel
import pl.kontroler.domain.DomainModule


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
class AppModule : KoinComponent {

    init {
        loadKoinModules(DomainModule().domainModule)
    }

    val appModule = module {
        viewModel { MainViewModel(get()) }
        viewModel { LoginViewModel(get()) }
        viewModel { RegisterViewModel(get()) }
        viewModel { HomeViewModel(get()) }
        viewModel { RefuelsViewModel(get(), get()) }
        viewModel { NewRefuelViewModel(get(), get(), get(), get()) }
        viewModel { EditCarDialogViewModel(get()) }
    }

}