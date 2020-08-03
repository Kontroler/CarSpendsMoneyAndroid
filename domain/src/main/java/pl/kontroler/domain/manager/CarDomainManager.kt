package pl.kontroler.domain.manager

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.kontroler.domain.mapper.CarMapper
import pl.kontroler.domain.model.Car
import pl.kontroler.firebase.manager.CarFirebaseManager
import pl.kontroler.firebase.util.Resource2


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

    suspend fun getCurrentCar(): Flow<Resource2<Car?>> {
        return carFirebaseManager.getCurrentCar().map {
            val carUid = (it as Resource2.Success).data!!.id
            val carFirebase = it.data!!.value
            val car = mapper.mapToModel(carUid, carFirebase)
            Resource2.Success(car)
        }
    }

    suspend fun getAll(): Flow<Resource2<List<Car>>> {
        return carFirebaseManager.getAll().map { resource ->
            val cars = (resource as Resource2.Success).data.map {
                val carUid = it.id
                val carFirebase = it.value
                mapper.mapToModel(carUid, carFirebase)
            }
            Resource2.Success(cars)
        }
    }

}