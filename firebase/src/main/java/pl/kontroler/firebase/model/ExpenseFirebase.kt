package pl.kontroler.firebase.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


/**
 * @author Rafał Nowowieski
 */

@IgnoreExtraProperties
data class ExpenseFirebase (
    var uid: String? = "",
    var description: String? = "",
    var quantity: String? = "",
    var unit: String? = "",
    var unitPrice: String? = "",
    var currency: String? = ""
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "description" to description,
            "quantity" to quantity,
            "unit" to unit,
            "unitPrice" to unitPrice,
            "currency" to currency
        )
    }
}