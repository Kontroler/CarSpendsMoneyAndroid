package pl.kontroler.carspendsmoney.ui.newRefuel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import pl.kontroler.carspendsmoney.R
import pl.kontroler.carspendsmoney.utils.SingleLiveEvent
import pl.kontroler.domain.manager.CarDomainManager
import pl.kontroler.domain.manager.CurrencyDomainManager
import pl.kontroler.domain.manager.FuelExpenseDomainManager
import pl.kontroler.domain.manager.FuelTypeDomainManager
import pl.kontroler.domain.model.*
import pl.kontroler.firebase.NextFuelExpenseCounterIsLowerException
import pl.kontroler.firebase.PreviousFuelExpenseCounterIsGreaterException
import pl.kontroler.firebase.util.Resource
import timber.log.Timber
import java.math.BigDecimal

@ExperimentalCoroutinesApi
class NewRefuelViewModel(
    private val fuelExpenseDomainManger: FuelExpenseDomainManager,
    private val currencyDomainManager: CurrencyDomainManager,
    private val fuelTypeDomainManager: FuelTypeDomainManager,
    private val carDomainManager: CarDomainManager
) : ViewModel() {

    val date = MutableLiveData<DateValue>().apply { value = DateValue.now() }
    val description = MutableLiveData<String>().apply { value = "" }
    val quantity = MutableLiveData<String>().apply { value = "0" }
    val unitPrice = MutableLiveData<String>().apply { value = "0" }
    var counter = MutableLiveData<String>().apply { value = "0" }

    private val _saveSuccess = SingleLiveEvent<Unit>()
    val saveSuccess: LiveData<Unit> = _saveSuccess

    private var _messageResource = SingleLiveEvent<MessageResource>()
    val messageResource: LiveData<MessageResource> = _messageResource

    val currenciesList = liveData {
        emit(Resource.Loading())

        try {
            val currencies = currencyDomainManager.all()
            emit(currencies)
        } catch (e: Exception) {
            Timber.e(e)
            _messageResource.value = MessageResource(
                MessageResource.Type.Error,
                R.string.newRefuel_loadingCurrenciesError
            )
            emit(Resource.Failure(e))
        }
    }
    val selectedCurrency = MutableLiveData<Currency>()

    val fuelTypesList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())

        try {
            val fuelTypes = fuelTypeDomainManager.all()
            emit(fuelTypes)
        } catch (e: Exception) {
            Timber.e(e)
            _messageResource.value = MessageResource(
                MessageResource.Type.Error,
                R.string.newRefuel_loadingFuelTypesError
            )
            emit(Resource.Failure(e))
        }
    }
    val selectedFuelType = MutableLiveData<FuelType>()

    fun write() {
        viewModelScope.launch {
            try {
                val fuelExpense = getFuelExpense()
                val car = carDomainManager.currentCar()

                fuelExpenseDomainManger.write(fuelExpense, car!!)
                _messageResource.value = MessageResource(
                    MessageResource.Type.Success,
                    R.string.newRefuel_saveSuccessful
                )
                _saveSuccess.call()
            } catch (e: PreviousFuelExpenseCounterIsGreaterException) {
                Timber.e(e)
                _messageResource.value = MessageResource(
                    MessageResource.Type.Error,
                    R.string.newRefuel_savingErrorCounterValueTooLow
                )
            } catch (e: NextFuelExpenseCounterIsLowerException) {
                Timber.e(e)
                _messageResource.value = MessageResource(
                    MessageResource.Type.Error,
                    R.string.newRefuel_savingErrorCounterValueTooHigh
                )
            } catch (e: Exception) {
                Timber.e(e)
                _messageResource.value = MessageResource(
                    MessageResource.Type.Error,
                    R.string.newRefuel_savingError
                )
            }
        }
    }

    private fun getFuelExpense(): FuelExpense {
        return FuelExpense.create(
            date = date.value!!,
            description = description.value!!,
            quantity = quantity.value!!.toBigDecimal(),
            unit = "L",
            unitPrice = unitPrice.value!!.toBigDecimal(),
            currency = selectedCurrency.value!!.code,
            totalPrice = calcTotalPrice(
                quantity.value!!.toBigDecimal(),
                unitPrice.value!!.toBigDecimal()
            ),
            counter = counter.value!!.toInt(),
            fuelType = selectedFuelType.value!!
        )
    }

    private fun calcTotalPrice(quantity: BigDecimal, unitPrice: BigDecimal): BigDecimal {
        return quantity.multiply(unitPrice).setScale(2, BigDecimal.ROUND_HALF_UP)
    }

    fun getTotalPriceString(quantity: String, unitPrice: String, currency: Currency?): String {
        val value = if (quantity.isEmpty() || unitPrice.isEmpty()) {
            calcTotalPrice(BigDecimal("0.00"), BigDecimal("0.00"))
        } else {
            calcTotalPrice(BigDecimal(quantity), BigDecimal(unitPrice))
        }
        return "$value ${currency?.code}"
    }
}