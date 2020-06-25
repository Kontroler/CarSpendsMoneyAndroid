package pl.kontroler.carspendsmoney.ui.home

import androidx.lifecycle.ViewModel
import pl.kontroler.domain.manager.RealtimeDatabaseDomainManager
import pl.kontroler.domain.model.Expense
import java.math.BigDecimal

class HomeViewModel(
    private val database: RealtimeDatabaseDomainManager
) : ViewModel() {

    fun write() {
        val expense = Expense(
            description = "Towar 1",
            currency = "PLN",
            quantity = BigDecimal("10.20"),
            unit = "szt.",
            unitPrice = BigDecimal("2.30")
        )
        database.writeExpense(expense)
    }

}