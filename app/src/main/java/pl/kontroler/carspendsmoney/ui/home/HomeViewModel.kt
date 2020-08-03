package pl.kontroler.carspendsmoney.ui.home

import androidx.lifecycle.ViewModel
import pl.kontroler.domain.manager.FuelExpenseDomainManager

class HomeViewModel(
    private val fuelExpenseDomainManger: FuelExpenseDomainManager
) : ViewModel() {

//    val allFuelExpenses
//        get() = liveData(Dispatchers.IO) {
//            emit(Resource.Loading())
//            try {
//                val data = fuelExpenseDomainManger.readAll()
//                emit(data)
//            } catch (e: Exception) {
//                Timber.e("ERROR: $e")
//                emit(Resource.Failure(e))
//            }
//        }
//
//    fun write() {
//        viewModelScope.launch {
//            val expense = FuelExpense.create(
//                date = DateValue.of(2020, 1, 1),
//                description = "Fuel",
//                quantity = BigDecimal("40.00"),
//                unit = "L",
//                unitPrice = BigDecimal("3.99"),
//                currency = "PLN",
//                totalPrice = BigDecimal("159.6"),
//                counter = 1000,
//                fuelType = "LPG"
//            )
//            try {
//                fuelExpenseDomainManger.write(expense)
//            } catch (e: Exception) {
//                Timber.e("Error $e")
//            }
//        }
//    }

}