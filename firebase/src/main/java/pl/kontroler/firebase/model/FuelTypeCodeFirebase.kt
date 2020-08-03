package pl.kontroler.firebase.model

import com.github.pozo.KotlinBuilder


/**
 * @author Rafał Nowowieski
 */

@KotlinBuilder
data class FuelTypeCodeFirebase(
    val type: String? = "",
    val code: String? = ""
)