package pl.kontroler.firebase.util

import java.math.BigDecimal


/**
 * @author Rafał Nowowieski
 */

inline fun <T> Iterable<T>.sumByBigDecimal(selector: (T) -> BigDecimal): BigDecimal {
    var sum = BigDecimal.ZERO
    for (element in this) {
        sum = sum.add(selector(element))
    }
    return sum
}