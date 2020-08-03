package pl.kontroler.domain.manager

import pl.kontroler.domain.mapper.CurrencyMapper
import pl.kontroler.domain.model.Currency
import pl.kontroler.firebase.manager.CurrencyFirebaseManager
import pl.kontroler.firebase.util.Resource2


/**
 * @author Rafał Nowowieski
 */

class CurrencyDomainManager(
    private val database: CurrencyFirebaseManager,
    private val mapper: CurrencyMapper
) {

    suspend fun readAll(): Resource2<List<Currency>> {
        val currencyResource = database.readAll() as Resource2.Success
        val mappedCurrenciesList = currencyResource.data
            .map { currencyFirebase -> mapper.mapToModel(currencyFirebase) }
        return Resource2.Success(mappedCurrenciesList)
    }

}