package pl.kontroler.carspendsmoney.ui.refuels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import pl.kontroler.domain.manager.FuelExpenseDomainManager
import pl.kontroler.firebase.util.Resource2
import timber.log.Timber
import java.lang.Exception

@ExperimentalCoroutinesApi
class RefuelsViewModel(
    fuelExpenseDomainManger: FuelExpenseDomainManager
) : ViewModel() {

    val fuelExpenses = liveData(Dispatchers.IO) {
        emit(Resource2.Loading())
        try {
            fuelExpenseDomainManger
                .readAllCoroutines()
                .collect { emit(it) }
        } catch (e: Exception) {
            Timber.e(e)
            emit(Resource2.Failure(e))
        }
    }

}