package pl.kontroler.firebase.model

import com.github.pozo.KotlinBuilder
import com.google.firebase.Timestamp


/**
 * @author Rafał Nowowieski
 */

@KotlinBuilder
data class ServiceExpenseFirebase(
    var date: Timestamp? = null,
    var name: String? = "",
    var description: String? = "",
    var currency: String? = "",
    var counter: Int? = 0,
    var totalPrice: Double? = null,
)