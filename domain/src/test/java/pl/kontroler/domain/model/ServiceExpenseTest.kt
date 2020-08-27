package pl.kontroler.domain.model

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.math.BigDecimal


/**
 * @author Rafał Nowowieski
 */

class ServiceExpenseTest {

    @Test
    fun `should create ServiceExpense`() {
        val serviceExpense = ServiceExpense.create(
            DateValue.of(2020, 10, 10),
            description = "Description",
            totalPrice = BigDecimal("10.00"),
            currency = "PLN",
            counter = 100_200,
            name = "Repair"
        )

        assertThat(serviceExpense, `is`(notNullValue()))
        assertThat(serviceExpense.date, `is`(DateValue.of(2020, 10, 10)))
        assertThat(serviceExpense.description, `is`("Description"))
        assertThat(serviceExpense.totalPrice, `is`(BigDecimal("10.00")))
        assertThat(serviceExpense.currency, `is`("PLN"))
        assertThat(serviceExpense.counter, `is`(100_200))
        assertThat(serviceExpense.name, `is`("Repair"))
    }

}