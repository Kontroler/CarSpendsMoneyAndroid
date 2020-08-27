package pl.kontroler.domain.model

import com.github.pozo.KotlinBuilder
import java.math.BigDecimal


/**
 * @author Rafał Nowowieski
 */

@KotlinBuilder
data class ServiceExpense(
    override val uid: String?,
    override val date: DateValue,
    override val description: String,
    override val totalPrice: BigDecimal,
    override val currency: String,
    override val counter: Int,
    val name: String,
) : BaseExpense(uid, date, description, totalPrice, currency, counter) {

    companion object {
        fun create(
            date: DateValue,
            description: String,
            totalPrice: BigDecimal,
            currency: String,
            counter: Int,
            name: String,
        ): ServiceExpense = ServiceExpense(
            null, date, description, totalPrice, currency, counter, name
        )
    }

}