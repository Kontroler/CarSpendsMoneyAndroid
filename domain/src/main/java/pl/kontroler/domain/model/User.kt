package pl.kontroler.domain.model

import com.github.pozo.KotlinBuilder


/**
 * @author Rafał Nowowieski
 */

@KotlinBuilder
data class User(
    val name: String,
    val email: String
)