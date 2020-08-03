package pl.kontroler.domain.model

import com.github.pozo.KotlinBuilder


/**
 * @author Rafał Nowowieski
 */

@KotlinBuilder
data class Car(
    val uid: String,
    val name: String,
    val counter: String,
    val isCurrent: Boolean
)