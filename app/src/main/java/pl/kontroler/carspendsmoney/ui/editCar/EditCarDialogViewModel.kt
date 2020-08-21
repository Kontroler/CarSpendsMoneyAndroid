package pl.kontroler.carspendsmoney.ui.editCar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import pl.kontroler.carspendsmoney.utils.SingleLiveEvent
import pl.kontroler.domain.manager.CarDomainManager
import pl.kontroler.domain.model.Car

@ExperimentalCoroutinesApi
class EditCarDialogViewModel(
    private val carDomainManager: CarDomainManager
) : ViewModel() {

    val name = MutableLiveData<String>().apply { value = "" }
    val counter = MutableLiveData<Int>().apply { value = 0 }
    val isCurrent = MutableLiveData<Boolean>().apply { value = false }

    private val _isSaved = SingleLiveEvent<Unit>()
    val isSaved: LiveData<Unit> = _isSaved

    fun save() {
        viewModelScope.launch {
            checkNotNull(name.value) { "Car name can not be null" }
            checkNotNull(counter.value) { "Counter can not be null" }
            checkNotNull(isCurrent.value) { "IsCurrent can not be null" }
            val car = Car.create(name.value!!, counter.value!!, isCurrent.value!!)
            carDomainManager.write(car)
            _isSaved.call()
        }
    }

}