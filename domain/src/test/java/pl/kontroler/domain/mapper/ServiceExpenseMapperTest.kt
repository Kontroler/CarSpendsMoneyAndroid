package pl.kontroler.domain.mapper

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mapstruct.factory.Mappers
import pl.kontroler.domain.model.DateValue
import pl.kontroler.domain.model.ServiceExpense
import pl.kontroler.firebase.model.ServiceExpenseFirebase
import java.math.BigDecimal


/**
 * @author Rafał Nowowieski
 */

class ServiceExpenseMapperTest {

    @Rule
    @JvmField
    val exception: ExpectedException = ExpectedException.none()

    @Test
    fun shouldSuccess_mapToFirebase() {
        val serviceExpense = ServiceExpense.create(
            date = DateValue.of(2020, 1, 1),
            description = "Description",
            totalPrice = BigDecimal("100.23"),
            currency = "PLN",
            counter = 100200,
            name = "Repair",
        )

        val mapper = Mappers.getMapper(ServiceExpenseMapper::class.java)
        val serviceExpenseFirebase = mapper.mapToFirebase(serviceExpense)

        assertThat(serviceExpenseFirebase.date!!.seconds, `is`(1577833200L))
        assertThat(serviceExpenseFirebase.description, `is`("Description"))
        assertThat(serviceExpenseFirebase.totalPrice, `is`(100.23))
        assertThat(serviceExpenseFirebase.currency, `is`("PLN"))
        assertThat(serviceExpenseFirebase.counter, `is`(100200))
        assertThat(serviceExpenseFirebase.name, `is`("Repair"))
    }

    @Test
    fun shouldSuccess_mapToModel() {
        val serviceExpenseFirebase = ServiceExpenseFirebase(
            date = DateValue.of(2020, 1, 1).timestamp,
            description = "Description",
            totalPrice = 100.23,
            currency = "PLN",
            counter = 100200,
            name = "Repair",
        )

        val mapper = Mappers.getMapper(ServiceExpenseMapper::class.java)
        val serviceExpense = mapper.mapToModel("123", serviceExpenseFirebase)

        assertThat(serviceExpense.uid, `is`("123"))
        assertThat(serviceExpense.date, `is`(DateValue.of(2020, 1, 1)))
        assertThat(serviceExpense.description, `is`("Description"))
        assertThat(serviceExpense.totalPrice, `is`(BigDecimal("100.23")))
        assertThat(serviceExpense.currency, `is`("PLN"))
        assertThat(serviceExpense.counter, `is`(100200))
        assertThat(serviceExpense.name, `is`("Repair"))
    }

}