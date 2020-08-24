package pl.kontroler.carspendsmoney.binding

import java.math.BigDecimal


/**
 * @author Rafał Nowowieski
 */

data class ExpensePieChartData(
    val value: BigDecimal,
    val currency: String,
    val type: Type
) {
    enum class Type {
        Fuel
    }
}