package pl.kontroler.domain

import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.mapstruct.factory.Mappers
import pl.kontroler.domain.manager.AuthenticationDomainManager
import pl.kontroler.domain.manager.RealtimeDatabaseDomainManager
import pl.kontroler.domain.mapper.ExpenseMapper
import pl.kontroler.domain.mapper.UserMapper
import pl.kontroler.firebase.firebaseModule


/**
 * @author Rafał Nowowieski
 */

class DomainModule : KoinComponent {

    init {
        loadKoinModules(firebaseModule)
    }

    val domainModule = module {

        single { AuthenticationDomainManager(get(), get()) }
        single { RealtimeDatabaseDomainManager(get(), get(), get()) }

        single<UserMapper> { Mappers.getMapper(UserMapper::class.java) }
        single<ExpenseMapper> { Mappers.getMapper(ExpenseMapper::class.java) }

    }

}