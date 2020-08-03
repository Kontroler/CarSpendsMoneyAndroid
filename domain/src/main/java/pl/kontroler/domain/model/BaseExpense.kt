package pl.kontroler.domain.model

import java.math.BigDecimal


/**
 * @author Rafał Nowowieski
 */

abstract class BaseExpense(
    open val uid: String?,
    open val date: DateValue,
    open val description: String,
    open val totalPrice: BigDecimal,
    open val currency: String,
    open val counter: Int
)