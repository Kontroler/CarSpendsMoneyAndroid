package pl.kontroler.domain.model

import com.github.pozo.KotlinBuilder


/**
 * @author Rafał Nowowieski
 */

@KotlinBuilder
data class FuelType(
    val type: String,
    val code: String
)