package pl.kontroler.carspendsmoney.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kontroler.domain.manager.CarDomainManager
import pl.kontroler.domain.model.Car
import pl.kontroler.firebase.util.Resource
import timber.log.Timber


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
class MainViewModel(
    private val carDomainManager: CarDomainManager
) : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable.stackTrace.toString())
    }

    val cars = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            carDomainManager
                .allFlow()
                .collect { emit(it) }
        } catch (e: Exception) {
            Timber.e(e)
            emit(Resource.Failure(e))
        }
    }

    val currentCar = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            carDomainManager
                .currentCarFlow()
                .collect { emit(it) }
        } catch (e: Exception) {
            Timber.e(e)
            emit(Resource.Failure(e))
        }
    }

    fun setCurrentCar(car: Car) {
        viewModelScope.launch(coroutineExceptionHandler) {
            carDomainManager.setCurrentCar(car)
        }
    }

}