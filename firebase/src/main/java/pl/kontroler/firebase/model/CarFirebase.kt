package pl.kontroler.firebase.model

import com.github.pozo.KotlinBuilder


/**
 * @author Rafał Nowowieski
 */

@KotlinBuilder
data class CarFirebase(
    val name: String? = null,
    val counter: Int? = null,
    val isCurrent: Boolean? = null
)