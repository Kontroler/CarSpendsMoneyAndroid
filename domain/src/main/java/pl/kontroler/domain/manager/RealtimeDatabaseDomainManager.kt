package pl.kontroler.domain.manager

import pl.kontroler.domain.mapper.ExpenseMapper
import pl.kontroler.domain.model.Expense
import pl.kontroler.firebase.manager.AuthenticationFirebaseManager
import pl.kontroler.firebase.manager.RealtimeDatabaseFirebaseManager


/**
 * @author Rafał Nowowieski
 */

class RealtimeDatabaseDomainManager(
    private val database: RealtimeDatabaseFirebaseManager,
    private val expenseMapper: ExpenseMapper,
    private val auth: AuthenticationFirebaseManager
) {

    fun writeExpense(expense: Expense) {
        val expenseFirebase = expenseMapper.mapToFirebase(expense).apply { uid = auth.currentUser.uid }
        database.writeExpense(expenseFirebase)
    }

}