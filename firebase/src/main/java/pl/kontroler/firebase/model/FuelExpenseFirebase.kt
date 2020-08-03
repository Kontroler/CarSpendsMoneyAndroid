package pl.kontroler.firebase.model

import com.github.pozo.KotlinBuilder
import com.google.firebase.Timestamp


/**
 * @author Rafał Nowowieski
 */

@KotlinBuilder
data class FuelExpenseFirebase(
    var date: Timestamp? = null,
    var description: String? = "",
    var quantity: String? = "",
    var unit: String? = "",
    var unitPrice: String? = "",
    var currency: String? = "",
    var counter: Int? = 0,
    var totalPrice: String? = "",
    var fuelType: FuelTypeCodeFirebase? = null
)