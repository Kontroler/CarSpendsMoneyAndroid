package pl.kontroler.carspendsmoney.ui.home

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import pl.kontroler.carspendsmoney.binding.ExpensePieChartData
import pl.kontroler.domain.manager.CarDomainManager
import pl.kontroler.domain.manager.FuelExpenseDomainManager
import pl.kontroler.domain.model.QueryDirection
import pl.kontroler.firebase.util.Resource
import timber.log.Timber
import java.math.BigDecimal

@ExperimentalCoroutinesApi
class HomeViewModel(
    private val carDomainManager: CarDomainManager,
    private val fuelExpenseDomainManger: FuelExpenseDomainManager
) : ViewModel() {

    val sumExpensePieChartData: LiveData<List<ExpensePieChartData>> = liveData(Dispatchers.IO) {
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
    }.map {
        val list = mutableListOf<ExpensePieChartData>()
        if (it is Resource.Success) {
            var sum = BigDecimal.ZERO
            it.data.forEach { fuelExpense ->
                sum = sum.add(fuelExpense.unitPrice.multiply(fuelExpense.quantity))
            }
            list.add(ExpensePieChartData(sum, it.data[0].currency, ExpensePieChartData.Type.Fuel))
        }
        list
    }

}