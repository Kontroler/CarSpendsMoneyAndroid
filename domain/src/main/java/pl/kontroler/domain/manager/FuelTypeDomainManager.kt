package pl.kontroler.domain.manager

import pl.kontroler.domain.model.FuelType
import pl.kontroler.firebase.manager.FuelTypeFirebaseManager
import pl.kontroler.firebase.util.Resource


/**
 * @author Rafał Nowowieski
 */

class FuelTypeDomainManager(
    private val database: FuelTypeFirebaseManager
) {

    suspend fun all(): Resource<List<FuelType>> {
        val returnedList = mutableListOf<FuelType>()
        val fuelTypesResource = database.all() as Resource.Success
        fuelTypesResource.data
            .forEach { fuelTypeFirebase ->
                fuelTypeFirebase.codes?.forEach { code ->
                    returnedList.add(FuelType(fuelTypeFirebase.type!!, code))
                }
            }
        return Resource.Success(returnedList)
    }

}