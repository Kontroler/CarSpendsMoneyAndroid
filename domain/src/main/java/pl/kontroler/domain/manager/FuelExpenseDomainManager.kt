package pl.kontroler.domain.manager

import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.kontroler.domain.mapper.FuelExpenseMapper
import pl.kontroler.domain.model.Car
import pl.kontroler.domain.model.FuelExpense
import pl.kontroler.domain.model.QueryDirection
import pl.kontroler.firebase.manager.FuelExpenseFirebaseManager
import pl.kontroler.firebase.util.Resource


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
class FuelExpenseDomainManager(
    private val database: FuelExpenseFirebaseManager,
    private val fuelExpenseMapper: FuelExpenseMapper
) {

    suspend fun write(fuelExpense: FuelExpense, car: Car) {
        val expenseFirebase = fuelExpenseMapper.mapToFirebase(fuelExpense)
        database.write(expenseFirebase, car.uid)
    }

    suspend fun allFlow(
        car: Car,
        queryDirection: QueryDirection
    ): Flow<Resource<List<FuelExpense>>> {
        val direction = if (queryDirection == QueryDirection.ASC) {
            Query.Direction.ASCENDING
        } else {
            Query.Direction.DESCENDING
        }
        return database
            .allFlow(car.uid, direction)
            .map { resource ->
                val fuelExpenseList = (resource as Resource.Success)
                    .data
                    .map { fuelExpenseFirebase ->
                        fuelExpenseMapper.mapToModel(
                            fuelExpenseFirebase.id,
                            fuelExpenseFirebase.value
                        )
                    }
                Resource.Success(fuelExpenseList)
            }
    }

    suspend fun delete(fuelExpense: FuelExpense, car: Car): Resource<Unit> {
        return database.delete(fuelExpense.uid!!, car.uid)
    }

}