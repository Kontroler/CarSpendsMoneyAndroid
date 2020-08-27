package pl.kontroler.domain.manager

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.*
import org.junit.Test
import org.mapstruct.factory.Mappers
import org.mockito.Mockito
import pl.kontroler.domain.mapper.ServiceExpenseMapper
import pl.kontroler.domain.model.Car
import pl.kontroler.domain.model.DateValue
import pl.kontroler.domain.model.QueryDirection
import pl.kontroler.domain.model.ServiceExpense
import pl.kontroler.firebase.manager.ServiceExpenseFirebaseManager
import pl.kontroler.firebase.model.ServiceExpenseFirebase
import pl.kontroler.firebase.util.IdValuePair
import pl.kontroler.firebase.util.Resource
import java.math.BigDecimal

/**
 * @author Rafał Nowowieski
 */

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class ServiceExpenseManagerTest {

    private fun <T> anyObject(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T

    private val mapper: ServiceExpenseMapper = Mappers.getMapper(ServiceExpenseMapper::class.java)
    private val serviceExpenseFirebaseManager: ServiceExpenseFirebaseManager =
        Mockito.mock(ServiceExpenseFirebaseManager::class.java)
    private val serviceExpenseDomainManager: ServiceExpenseDomainManager =
        ServiceExpenseDomainManager(serviceExpenseFirebaseManager, mapper)

    private val testDispatcher = TestCoroutineDispatcher()

    @Test
    fun shouldReturnResourceSuccess_write() = runBlockingTest {
        Mockito.`when`(serviceExpenseFirebaseManager.write(
            anyObject(),
            anyObject()
        )).thenReturn(Resource.Success(Unit))

        val serviceExpense = ServiceExpense.create(
            date = DateValue.of(2020, 1, 1),
            description = "Description",
            totalPrice = BigDecimal("100.23"),
            currency = "PLN",
            counter = 100200,
            name = "Repair"
        )
        val car = Car(
            uid = "123",
            name = "Car 1",
            counter = 100200,
            isCurrent = true
        )

        val returnedValue = serviceExpenseDomainManager.write(serviceExpense, car)
        assertThat(returnedValue::class, `is`(Resource.Success::class))
    }

    @Test
    fun shouldReturnResourceFailure_write() = runBlockingTest {
        Mockito.`when`(serviceExpenseFirebaseManager.write(
            anyObject(),
            anyObject()
        )).thenReturn(Resource.Failure(Error("error")))

        val serviceExpense = ServiceExpense.create(
            date = DateValue.of(2020, 1, 1),
            description = "Description",
            totalPrice = BigDecimal("100.23"),
            currency = "PLN",
            counter = 100200,
            name = "Repair"
        )
        val car = Car(
            uid = "123",
            name = "Car 1",
            counter = 100200,
            isCurrent = true
        )

        val returnedValue = serviceExpenseDomainManager.write(serviceExpense, car)
        assertThat(returnedValue::class, `is`(Resource.Failure::class))
    }

    @Test
    fun shouldReturnResourceSuccess_delete() = runBlockingTest {
        Mockito.`when`(serviceExpenseFirebaseManager.delete(
            anyObject(),
            anyObject()
        )).thenReturn(Resource.Success(Unit))

        val serviceExpense = ServiceExpense(
            uid = "123",
            date = DateValue.of(2020, 1, 1),
            description = "Description",
            totalPrice = BigDecimal("100.23"),
            currency = "PLN",
            counter = 100200,
            name = "Repair"
        )
        val car = Car(
            uid = "123",
            name = "Car 1",
            counter = 100200,
            isCurrent = true
        )

        val returnedValue = serviceExpenseDomainManager.delete(serviceExpense, car)
        assertThat(returnedValue::class, `is`(Resource.Success::class))
    }

    @Test
    fun shouldReturnResourceFailure_delete() = runBlockingTest {
        Mockito.`when`(serviceExpenseFirebaseManager.delete(
            anyObject(),
            anyObject()
        )).thenReturn(Resource.Failure(Error("error")))

        val serviceExpense = ServiceExpense(
            uid = "123",
            date = DateValue.of(2020, 1, 1),
            description = "Description",
            totalPrice = BigDecimal("100.23"),
            currency = "PLN",
            counter = 100200,
            name = "Repair"
        )
        val car = Car(
            uid = "123",
            name = "Car 1",
            counter = 100200,
            isCurrent = true
        )

        val returnedValue = serviceExpenseDomainManager.delete(serviceExpense, car)
        assertThat(returnedValue::class, `is`(Resource.Failure::class))
    }

    @Test
    fun shouldReturnResourceSuccess_allFlow() = runBlockingTest {
        val list = listOf(
            IdValuePair(
                "123",
                ServiceExpenseFirebase(
                    date = DateValue.of(2020, 1, 1).timestamp,
                    name = "Repair",
                    description = "Description",
                    currency = "PLN",
                    counter = 100_200,
                    totalPrice = 120.50
                ))
        )

        Mockito.`when`(serviceExpenseFirebaseManager.allFlow(
            anyObject(),
            anyObject()
        )).thenReturn(flow { emit(Resource.Success(list)) })

        val car = Car(
            uid = "1234",
            name = "Car 1",
            counter = 100_200,
            isCurrent = true
        )

        val returnedValue = serviceExpenseDomainManager.allFlow(car, QueryDirection.DESC)
        val resource = returnedValue.single()
        assertThat(resource::class, `is`(Resource.Success::class))
        val resourceData = (resource as Resource.Success).data
        assertThat(resourceData.size, `is`(1))
        assertThat(resourceData[0].name, `is`("Repair"))
        assertThat(resourceData[0].uid, `is`("123"))
        assertThat(resourceData[0].date, `is`(DateValue.of(2020, 1, 1)))
        assertThat(resourceData[0].description, `is`("Description"))
        assertThat(resourceData[0].currency, `is`("PLN"))
        assertThat(resourceData[0].counter, `is`(100_200))
        assertThat(resourceData[0].totalPrice, `is`(BigDecimal(120.50)))
    }

    @Test
    fun shouldReturnResourceFailure_allFlow() = runBlockingTest {
        Mockito.`when`(serviceExpenseFirebaseManager.allFlow(
            anyObject(),
            anyObject()
        )).thenReturn(flowOf(Resource.Failure(Error("Error test"))))

        val car = Car(
            uid = "1234",
            name = "Car 1",
            counter = 100200,
            isCurrent = true
        )

        val returnedValue = serviceExpenseDomainManager.allFlow(car, QueryDirection.DESC)
        val resource = returnedValue.single()
        assertThat(resource::class, `is`(Resource.Failure::class))
        val resourceError = (resource as Resource.Failure).throwable
        assertThat(resourceError.message, `is`("Error test"))
    }

}