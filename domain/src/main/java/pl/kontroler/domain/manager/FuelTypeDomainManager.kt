package pl.kontroler.domain.manager

import pl.kontroler.domain.model.FuelType
import pl.kontroler.firebase.manager.FuelTypeFirebaseManager
import pl.kontroler.firebase.util.Resource2


/**
 * @author Rafał Nowowieski
 */

class FuelTypeDomainManager(
    private val database: FuelTypeFirebaseManager
) {

    suspend fun readAll(): Resource2<List<FuelType>> {
        val returnedList = mutableListOf<FuelType>()
        val fuelTypesResource = database.readAll() as Resource2.Success
        fuelTypesResource.data
            .forEach { fuelTypeFirebase ->
                fuelTypeFirebase.codes?.forEach { code ->
                    returnedList.add(FuelType(fuelTypeFirebase.type!!, code))
                }
            }
        return Resource2.Success(returnedList)
    }

}