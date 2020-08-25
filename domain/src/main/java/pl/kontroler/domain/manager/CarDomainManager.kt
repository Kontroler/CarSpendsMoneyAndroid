package pl.kontroler.domain.manager

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.kontroler.domain.mapper.CarMapper
import pl.kontroler.domain.model.Car
import pl.kontroler.domain.utils.MissingCarException
import pl.kontroler.firebase.manager.CarFirebaseManager
import pl.kontroler.firebase.model.CarFirebase
import pl.kontroler.firebase.util.IdValuePair
import pl.kontroler.firebase.util.Resource


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
class CarDomainManager(
    private val carFirebaseManager: CarFirebaseManager,
    private val mapper: CarMapper
) {

    suspend fun write(car: Car) {
        carFirebaseManager.write(mapper.mapToFirebase(car))
    }

    suspend fun currentCar(): Car {
        val currentCarFirebase: IdValuePair<CarFirebase> = carFirebaseManager.currentCar()
            ?: throw MissingCarException("Current car is null")

        val carUid = currentCarFirebase.id
        val carFirebase = currentCarFirebase.value

        return mapper.mapToModel(carUid, carFirebase)
    }

    suspend fun currentCarFlow(): Flow<Resource<Car>> {
        return carFirebaseManager.currentCarFlow().map {
            val carUid = (it as Resource.Success).data!!.id
            val carFirebase = it.data!!.value
            val car = mapper.mapToModel(carUid, carFirebase)
            Resource.Success(car)
        }
    }

    suspend fun allFlow(): Flow<Resource<List<Car>>> {
        return carFirebaseManager.allFlow().map { resource ->
            val cars = (resource as Resource.Success).data.map {
                val carUid = it.id
                val carFirebase = it.value
                mapper.mapToModel(carUid, carFirebase)
            }
            Resource.Success(cars)
        }
    }

    suspend fun setCurrentCar(car: Car) {
        carFirebaseManager.setCurrentCar(car.uid)
    }

}