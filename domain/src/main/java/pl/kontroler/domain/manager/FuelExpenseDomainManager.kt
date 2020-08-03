package pl.kontroler.domain.manager

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import pl.kontroler.domain.mapper.FuelExpenseMapper
import pl.kontroler.domain.model.FuelExpense
import pl.kontroler.firebase.manager.FuelExpenseFirebaseManager
import pl.kontroler.firebase.util.Resource2
import timber.log.Timber
import java.lang.Exception


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
class FuelExpenseDomainManager(
    private val database: FuelExpenseFirebaseManager,
    private val fuelExpenseMapper: FuelExpenseMapper
) {

    suspend fun write(fuelExpense: FuelExpense) {
        val expenseFirebase = fuelExpenseMapper.mapToFirebase(fuelExpense)
        database.write(expenseFirebase)
    }

//    fun readAll(): LiveData<List<FuelExpense>> {
//        return Transformations.map(database.readAll()) {
//            it.map { fuelExpenseFirebase ->
//                fuelExpenseMapper.mapToModel(
//                    fuelExpenseFirebase.first,
//                    fuelExpenseFirebase.second
//                )
//            }
//        }
//    }

    suspend fun readAll(): Resource2<List<FuelExpense>> {
        val fuelExpenseFirebaseResource = database.readAll() as Resource2.Success

        val mappedFuelExpensesList = fuelExpenseFirebaseResource.data.map { fuelExpenseFirebase ->
            fuelExpenseMapper.mapToModel(
                fuelExpenseFirebase.id,
                fuelExpenseFirebase.value
            )
        }

        return Resource2.Success(mappedFuelExpensesList)
    }

    suspend fun readAllCoroutines(): Flow<Resource2<List<FuelExpense>>> {
        return database
            .readAllCoroutines()
            .map { resource ->
                val fuelExpenseList = (resource as Resource2.Success)
                    .data
                    .map { fuelExpenseFirebase ->
                        fuelExpenseMapper.mapToModel(
                            fuelExpenseFirebase.id,
                            fuelExpenseFirebase.value
                        )
                    }
                Resource2.Success(fuelExpenseList)
            }
    }

}