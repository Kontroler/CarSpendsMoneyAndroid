package pl.kontroler.domain.mapper

import org.mapstruct.Mapper
import pl.kontroler.domain.model.Expense
import pl.kontroler.firebase.model.ExpenseFirebase


/**
 * @author Rafał Nowowieski
 */

@Mapper
interface ExpenseMapper {

    fun mapToFirebase(expense: Expense): ExpenseFirebase

}