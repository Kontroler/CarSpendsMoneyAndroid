package pl.kontroler.firebase.manager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import pl.kontroler.firebase.model.ExpenseFirebase
import timber.log.Timber


/**
 * @author Rafał Nowowieski
 */

class RealtimeDatabaseFirebaseManager(
    private val database: DatabaseReference
) {

    fun writeExpense(expense: ExpenseFirebase) {
        val key = database.child("expenses").push().key
        if (key == null) {
            Timber.w("Couldn't get push key for expenses")
            return
        }

        val expenseValues = expense.toMap()

        val childUpdates = HashMap<String, Any>()
        childUpdates["expenses/$key"] = expenseValues
        childUpdates["user-expenses/${expense.uid}/$key"] = expenseValues

        database.updateChildren(childUpdates)
    }

}