package pl.kontroler.firebase.manager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.channelFlow
import pl.kontroler.firebase.model.FuelExpenseFirebase
import timber.log.Timber


/**
 * @author Rafał Nowowieski
 */

class RealtimeDatabaseFirebaseManager(
    private val database: DatabaseReference,
    private val auth: FirebaseAuth
) {

    fun writeExpense(fuelExpense: FuelExpenseFirebase) {
        val key = database.child("expenses").push().key
        if (key == null) {
            Timber.w("Couldn't get push key for expenses")
            return
        }

        val user = auth.currentUser ?: throw Error("You cannot get user if you not logged.")

//        val expenseValues = fuelExpense.toMap()
//        val childUpdates = HashMap<String, Any>()
//        childUpdates["expenses/$key"] = expenseValues
//        childUpdates["user-expenses/${user.uid}/$key"] = expenseValues
//
//        database.updateChildren(childUpdates)
    }

}