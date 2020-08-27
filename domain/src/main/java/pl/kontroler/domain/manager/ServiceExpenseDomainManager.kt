package pl.kontroler.domain.manager

import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.kontroler.domain.mapper.ServiceExpenseMapper
import pl.kontroler.domain.model.Car
import pl.kontroler.domain.model.QueryDirection
import pl.kontroler.domain.model.ServiceExpense
import pl.kontroler.firebase.manager.ServiceExpenseFirebaseManager
import pl.kontroler.firebase.util.Resource


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
class ServiceExpenseDomainManager(
    private val database: ServiceExpenseFirebaseManager,
    private val mapper: ServiceExpenseMapper,
) {

    suspend fun write(serviceExpense: ServiceExpense, car: Car): Resource<Unit> {
        val serviceExpenseFirebase = mapper.mapToFirebase(serviceExpense)
        return database.write(serviceExpenseFirebase, car.uid)
    }

    suspend fun allFlow(
        car: Car,
        queryDirection: QueryDirection,
    ): Flow<Resource<List<ServiceExpense>>> {
        val direction = if (queryDirection == QueryDirection.ASC) {
            Query.Direction.ASCENDING
        } else {
            Query.Direction.DESCENDING
        }
        return database.allFlow(car.uid, direction).map { resource ->
            if (resource is Resource.Success) {
                val serviceExpenseList = resource
                    .data
                    .map { serviceExpenseFirebase ->
                        mapper.mapToModel(
                            serviceExpenseFirebase.id,
                            serviceExpenseFirebase.value
                        )
                    }
                Resource.Success(serviceExpenseList)
            } else {
                Resource.Failure((resource as Resource.Failure).throwable)
            }
        }
    }

    suspend fun delete(serviceExpense: ServiceExpense, car: Car): Resource<Unit> {
        return database.delete(serviceExpense.uid!!, car.uid)
    }

}