package pl.kontroler.domain.manager

import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import pl.kontroler.domain.mapper.ServiceExpenseMapper
import pl.kontroler.domain.model.Car
import pl.kontroler.domain.model.QueryDirection
import pl.kontroler.domain.model.ServiceExpense
import pl.kontroler.firebase.manager.CarFirebaseManager
import pl.kontroler.firebase.manager.ServiceExpenseFirebaseManager
import pl.kontroler.firebase.model.CarFirebase
import pl.kontroler.firebase.model.ServiceExpenseFirebase
import pl.kontroler.firebase.util.IdValuePair
import pl.kontroler.firebase.util.Resource


/**
 * @author Rafał Nowowieski
 */

@FlowPreview
@ExperimentalCoroutinesApi
class ServiceExpenseDomainManager(
    private val carFirebaseManager: CarFirebaseManager,
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

    suspend fun allCurrentCarFlow(
        queryDirection: QueryDirection,
    ): Flow<Resource<List<ServiceExpense>>> {
        val direction = if (queryDirection == QueryDirection.ASC) {
            Query.Direction.ASCENDING
        } else {
            Query.Direction.DESCENDING
        }

        return carFirebaseManager.currentCarFlow()
            .map { carResource ->
                if (carResource is Resource.Success) {
                    database.allFlow(carResource.data!!.id, direction)
                } else {
                    Resource.Failure<IdValuePair<ServiceExpenseFirebase>>((carResource as Resource.Failure).throwable)
                }
            }
            .flatMapLatest { value ->
                flow {
                    if (value is Resource.Success<*>) {
                        @Suppress("UNCHECKED_CAST") val serviceExpenseList = (value
                            .data as List<*>)
                            .map { serviceExpenseFirebase ->
                                serviceExpenseFirebase as IdValuePair<ServiceExpenseFirebase>
                                mapper.mapToModel(
                                    serviceExpenseFirebase.id,
                                    serviceExpenseFirebase.value
                                )
                            }
                        emit(Resource.Success(serviceExpenseList))
                    } else {
                        emit(Resource.Failure<List<ServiceExpense>>((value as Resource.Failure<*>).throwable))
                    }
                }
            }
    }

    suspend fun delete(serviceExpense: ServiceExpense, car: Car): Resource<Unit> {
        return database.delete(serviceExpense.uid!!, car.uid)
    }

}