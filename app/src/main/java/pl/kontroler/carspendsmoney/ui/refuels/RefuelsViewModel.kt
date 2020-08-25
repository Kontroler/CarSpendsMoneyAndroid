package pl.kontroler.carspendsmoney.ui.refuels

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kontroler.carspendsmoney.R
import pl.kontroler.carspendsmoney.utils.SingleLiveEvent
import pl.kontroler.domain.manager.CarDomainManager
import pl.kontroler.domain.manager.FuelExpenseDomainManager
import pl.kontroler.domain.model.FuelExpense
import pl.kontroler.domain.model.MessageResource
import pl.kontroler.domain.model.QueryDirection
import pl.kontroler.firebase.util.Resource
import timber.log.Timber

@ExperimentalCoroutinesApi
class RefuelsViewModel(
    private val fuelExpenseDomainManger: FuelExpenseDomainManager,
    private val carDomainManager: CarDomainManager,
) : ViewModel() {

    private val _messageResource = SingleLiveEvent<MessageResource>()
    val messageResource: LiveData<MessageResource> get() = _messageResource

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

    val delete: (fuelExpense: FuelExpense) -> Unit = { fuelExpense ->
        viewModelScope.launch {
            carDomainManager.currentCar().runCatching { this }
                .onSuccess { car ->
                    val deleted = fuelExpenseDomainManger.delete(fuelExpense, car)
                    if (deleted is Resource.Success) {
                        _messageResource.value = MessageResource(
                            MessageResource.Type.Error,
                            R.string.refuels_deleteRefuelSuccessful
                        )
                    } else {
                        deleted as Resource.Failure
                        Timber.e(deleted.throwable)
                        _messageResource.value = MessageResource(
                            MessageResource.Type.Error,
                            R.string.refuels_deleteRefuelError
                        )
                    }
                }.onFailure { error ->
                    Timber.e(error)
                    _messageResource.value = MessageResource(
                        MessageResource.Type.Error,
                        R.string.refuels_deleteRefuelMissingCurrentError
                    )
                }
        }
    }

}