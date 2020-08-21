package pl.kontroler.domain.manager

import pl.kontroler.domain.mapper.CurrencyMapper
import pl.kontroler.domain.model.Currency
import pl.kontroler.firebase.manager.CurrencyFirebaseManager
import pl.kontroler.firebase.util.Resource


/**
 * @author Rafał Nowowieski
 */

class CurrencyDomainManager(
    private val database: CurrencyFirebaseManager,
    private val mapper: CurrencyMapper
) {

    suspend fun all(): Resource<List<Currency>> {
        val currencyResource = database.all() as Resource.Success
        val mappedCurrenciesList = currencyResource.data
            .map { currencyFirebase -> mapper.mapToModel(currencyFirebase) }
        return Resource.Success(mappedCurrenciesList)
    }

}