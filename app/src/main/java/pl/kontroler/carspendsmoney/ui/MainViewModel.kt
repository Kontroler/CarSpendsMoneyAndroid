package pl.kontroler.carspendsmoney.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import pl.kontroler.domain.manager.CarDomainManager
import pl.kontroler.firebase.util.Resource2
import timber.log.Timber


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
class MainViewModel(
    private val carDomainManager: CarDomainManager
) : ViewModel() {

    val cars = liveData(Dispatchers.IO) {
        emit(Resource2.Loading())
        try {
            carDomainManager
                .getAll()
                .collect { emit(it) }
        } catch (e: Exception) {
            Timber.e(e)
            emit(Resource2.Failure(e))
        }
    }

}