package pl.kontroler.domain.mapper

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mapstruct.factory.Mappers
import pl.kontroler.domain.model.DateValue
import pl.kontroler.domain.model.FuelExpense
import pl.kontroler.domain.model.FuelType
import pl.kontroler.firebase.model.FuelExpenseFirebase
import pl.kontroler.firebase.model.FuelTypeCodeFirebase
import java.math.BigDecimal


/**
 * @author Rafał Nowowieski
 */

class FuelExpenseMapperTest {

    @Rule
    @JvmField
    val exception: ExpectedException = ExpectedException.none()

    @Test
    fun shouldSuccess_mapToFirebase() {
        val fuelExpense = FuelExpense.create(
            date = DateValue.of(2020, 1, 1),
            description = "Fuel",
            quantity = BigDecimal("40.00"),
            unit = "L",
            unitPrice = BigDecimal("3.99"),
            currency = "PLN",
            totalPrice = BigDecimal("159.6"),
            counter = 1000,
            fuelType = FuelType("Gas", "LPG")
        )

        val mapper = Mappers.getMapper(FuelExpenseMapper::class.java)
        val expenseFirebase = mapper.mapToFirebase(fuelExpense)

        assertThat(expenseFirebase.description, `is`("Fuel"))
        assertThat(expenseFirebase.quantity, `is`("40.00"))
        assertThat(expenseFirebase.unit, `is`("L"))
        assertThat(expenseFirebase.unitPrice, `is`("3.99"))
        assertThat(expenseFirebase.currency, `is`("PLN"))
        assertThat(expenseFirebase.date!!.seconds, `is`(1577833200L))
    }

    @Test
    fun shouldSuccess_mapToModel() {
        val fuelExpenseFirebase = FuelExpenseFirebase(
            date = DateValue.of(2020, 1, 1).timestamp,
            description = "E95",
            quantity = "10.00",
            unit = "L",
            unitPrice = "3.99",
            currency = "PLN",
            counter = 1234,
            totalPrice = "39.90",
            fuelType = FuelTypeCodeFirebase("Gas", "LPG")
        )

        val mapper = Mappers.getMapper(FuelExpenseMapper::class.java)
        val expense = mapper.mapToModel("1", fuelExpenseFirebase)

        assertThat(expense.uid, `is`("1"))
        assertThat(expense.date, `is`(DateValue.of(2020, 1, 1)))
        assertThat(expense.description, `is`("E95"))
        assertThat(expense.quantity, `is`(BigDecimal("10.00")))
        assertThat(expense.unit, `is`("L"))
        assertThat(expense.unitPrice, `is`(BigDecimal("3.99")))
        assertThat(expense.currency, `is`("PLN"))
        assertThat(expense.counter, `is`(1234))
        assertThat(expense.totalPrice, `is`(BigDecimal("39.90")))
    }

    @Test
    fun shouldThrowIllegalArgumentException_mapToModel() {
        val fuelExpenseFirebase = FuelExpenseFirebase(
            date = DateValue.of(2020, 1, 1).timestamp,
            description = "E95",
            quantity = "10.00",
            unit = "L",
            unitPrice = "3.99",
            currency = "PLN",
            counter = 1234,
            totalPrice = null
        )

        val mapper = Mappers.getMapper(FuelExpenseMapper::class.java)
        exception.expect(IllegalArgumentException::class.java)
        mapper.mapToModel("1", fuelExpenseFirebase)
    }

}