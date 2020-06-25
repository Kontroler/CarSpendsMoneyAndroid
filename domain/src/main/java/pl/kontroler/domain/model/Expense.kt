package pl.kontroler.domain.model

import java.math.BigDecimal


/**
 * @author Rafał Nowowieski
 */

data class Expense(
    val description: String,
    val quantity: BigDecimal,
    val unit: String,
    val unitPrice: BigDecimal,
    val currency: String
)