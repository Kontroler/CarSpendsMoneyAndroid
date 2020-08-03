package pl.kontroler.domain.model

import com.github.pozo.KotlinBuilder
import java.math.BigDecimal


/**
 * @author Rafał Nowowieski
 */

@KotlinBuilder
data class FuelExpense(
    override val uid: String?,
    override val date: DateValue,
    override val description: String,
    override val totalPrice: BigDecimal,
    override val currency: String,
    override val counter: Int,
    val quantity: BigDecimal,
    val unit: String,
    val unitPrice: BigDecimal,
    val fuelType: FuelType
) : BaseExpense(uid, date, description, totalPrice, currency, counter) {

    companion object {
        fun create(
            date: DateValue,
            description: String,
            totalPrice: BigDecimal,
            currency: String,
            counter: Int,
            quantity: BigDecimal,
            unit: String,
            unitPrice: BigDecimal,
            fuelType: FuelType
        ): FuelExpense =
            FuelExpense(
                null,
                date,
                description,
                totalPrice,
                currency,
                counter,
                quantity,
                unit,
                unitPrice,
                fuelType
            )
    }
}
