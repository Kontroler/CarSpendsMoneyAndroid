package pl.kontroler.carspendsmoney.ui.refuels

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import pl.kontroler.domain.manager.CarDomainManager
import pl.kontroler.domain.manager.FuelExpenseDomainManager
import pl.kontroler.domain.model.QueryDirection
import pl.kontroler.firebase.util.Resource
import timber.log.Timber
import java.lang.Exception

@ExperimentalCoroutinesApi
class RefuelsViewModel(
    fuelExpenseDomainManger: FuelExpenseDomainManager,
    carDomainManager: CarDomainManager
) : ViewModel() {

    val fuelExpenses = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            carDomainManager
                .currentCarFlow()
                .collect { emit(it) }
        } catch (e: Exception) {
            Timber.e(e)
            emit(Resource.Failure(e))
        }
    }.switchMap { currentCar ->
        liveData(Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                if (currentCar is Resource.Success) {
                    fuelExpenseDomainManger
                        .allFlow(currentCar.data, QueryDirection.DESC)
                        .collect { emit(it) }
                }
            } catch (e: Exception) {
                Timber.e(e)
                emit(Resource.Failure(e))
            }
        }
    }

}