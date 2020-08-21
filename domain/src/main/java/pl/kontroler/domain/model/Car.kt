package pl.kontroler.domain.model

import com.github.pozo.KotlinBuilder


/**
 * @author Rafał Nowowieski
 */

@KotlinBuilder
data class Car(
    val uid: String,
    val name: String,
    val counter: Int,
    val isCurrent: Boolean
) {
    companion object {
        fun create(
            name: String,
            counter: Int,
            isCurrent: Boolean
        ): Car = Car("", name, counter, isCurrent)
    }
}