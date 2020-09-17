package pl.kontroler.carspendsmoney.ui.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kontroler.carspendsmoney.R
import pl.kontroler.carspendsmoney.utils.SingleLiveEvent
import pl.kontroler.domain.manager.CarDomainManager
import pl.kontroler.domain.manager.ServiceExpenseDomainManager
import pl.kontroler.domain.model.FuelExpense
import pl.kontroler.domain.model.MessageResource
import pl.kontroler.domain.model.QueryDirection
import pl.kontroler.domain.model.ServiceExpense
import pl.kontroler.firebase.util.Resource
import timber.log.Timber
import java.lang.Exception

@FlowPreview
@ExperimentalCoroutinesApi
class ServicesViewModel(
    private val serviceExpenseDomainManager: ServiceExpenseDomainManager,
    private val carDomainManager: CarDomainManager,
) : ViewModel() {

    private val _messageResource = SingleLiveEvent<MessageResource>()
    val messageResource: LiveData<MessageResource> get() = _messageResource

    val serviceExpenses = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            serviceExpenseDomainManager
                .allCurrentCarFlow(QueryDirection.DESC)
                .collect { emit(it) }
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    val delete: (serviceExpense: ServiceExpense) -> Unit = { serviceExpense ->
        viewModelScope.launch {
            carDomainManager.currentCar().runCatching { this }
                .onSuccess { car ->
                    val deleted = serviceExpenseDomainManager.delete(serviceExpense, car)
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