package pl.kontroler.carspendsmoney.ui.newRefuel

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import pl.kontroler.carspendsmoney.utils.SingleLiveEvent
import pl.kontroler.domain.manager.CurrencyDomainManager
import pl.kontroler.domain.manager.FuelExpenseDomainManager
import pl.kontroler.domain.manager.FuelTypeDomainManager
import pl.kontroler.domain.model.Currency
import pl.kontroler.domain.model.DateValue
import pl.kontroler.domain.model.FuelExpense
import pl.kontroler.domain.model.FuelType
import pl.kontroler.firebase.util.Resource2
import timber.log.Timber
import java.math.BigDecimal

@ExperimentalCoroutinesApi
class NewRefuelViewModel(
    private val fuelExpenseDomainManger: FuelExpenseDomainManager,
    private val currencyDomainManager: CurrencyDomainManager,
    private val fuelTypeDomainManager: FuelTypeDomainManager
) : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable.stackTrace.toString())
    }

    val date = MutableLiveData<DateValue>().apply { value = DateValue.now() }
    val description = MutableLiveData<String>().apply { value = "" }
    val quantity = MutableLiveData<String>().apply { value = "0" }
    val unitPrice = MutableLiveData<String>().apply { value = "0" }
    var counter = MutableLiveData<String>().apply { value = "0" }

    private val _saveSuccess = SingleLiveEvent<Unit>()
    val saveSuccess: LiveData<Unit> = _saveSuccess

    val currenciesList = liveData {
        emit(Resource2.Loading())

        try {
            val currencies = currencyDomainManager.readAll()
            emit(currencies)
        } catch (e: Exception) {
            Timber.e(e)
            emit(Resource2.Failure(e))
        }
    }
    val selectedCurrency = MutableLiveData<Currency>()

    val fuelTypesList = liveData(Dispatchers.IO) {
        emit(Resource2.Loading())

        try {
            val fuelTypes = fuelTypeDomainManager.readAll()
            emit(fuelTypes)
        } catch (e: Exception) {
            Timber.e(e)
            emit(Resource2.Failure(e))
        }
    }
    val selectedFuelType = MutableLiveData<FuelType>()

    fun write() {
        checkNotNull(date.value) { "Date is null" }
        checkNotNull(description.value) { "Description is null" }
        checkNotNull(quantity.value) { "Quantity is null" }
        checkNotNull(unitPrice.value) { "Unit price is null" }
        checkNotNull(selectedCurrency.value) { "Selected currency is null" }
        checkNotNull(counter.value) { "Counter is null" }
        checkNotNull(selectedFuelType.value) { "Selected fuel type is null" }

        viewModelScope.launch(coroutineExceptionHandler) {
            val fuelExpense = FuelExpense.create(
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
            fuelExpenseDomainManger.write(fuelExpense)
            _saveSuccess.call()
        }
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