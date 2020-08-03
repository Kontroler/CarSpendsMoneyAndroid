package pl.kontroler.domain.manager

import pl.kontroler.domain.mapper.FuelExpenseMapper
import pl.kontroler.domain.model.FuelExpense
import pl.kontroler.firebase.manager.RealtimeDatabaseFirebaseManager


/**
 * @author Rafał Nowowieski
 */

class RealtimeDatabaseDomainManager(
    private val database: RealtimeDatabaseFirebaseManager,
    private val fuelExpenseMapper: FuelExpenseMapper
) {

    fun writeExpense(fuelExpense: FuelExpense) {
        val expenseFirebase = fuelExpenseMapper.mapToFirebase(fuelExpense)
        database.writeExpense(expenseFirebase)
    }

}