package pl.kontroler.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.mapstruct.factory.Mappers
import pl.kontroler.domain.manager.*
import pl.kontroler.domain.mapper.CarMapper
import pl.kontroler.domain.mapper.CurrencyMapper
import pl.kontroler.domain.mapper.FuelExpenseMapper
import pl.kontroler.domain.mapper.UserMapper
import pl.kontroler.firebase.firebaseModule


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
class DomainModule : KoinComponent {

    init {
        loadKoinModules(firebaseModule)
    }

    val domainModule = module {

        single { AuthenticationDomainManager(get(), get()) }
        single { FuelExpenseDomainManager(get(), get()) }
        single { CurrencyDomainManager(get(), get()) }
        single { FuelTypeDomainManager(get()) }
        single { CarDomainManager(get(), get()) }

        single<UserMapper> { Mappers.getMapper(UserMapper::class.java) }
        single<FuelExpenseMapper> { Mappers.getMapper(FuelExpenseMapper::class.java) }
        single<CurrencyMapper> { Mappers.getMapper(CurrencyMapper::class.java) }
        single<CarMapper> { Mappers.getMapper(CarMapper::class.java) }
    }

}