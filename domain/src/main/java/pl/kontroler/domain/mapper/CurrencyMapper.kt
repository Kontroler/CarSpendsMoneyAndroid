package pl.kontroler.domain.mapper

import org.mapstruct.Mapper
import pl.kontroler.domain.model.Currency
import pl.kontroler.firebase.model.CurrencyFirebase


/**
 * @author Rafał Nowowieski
 */

@Mapper
interface CurrencyMapper {

    fun mapToModel(currencyFirebase: CurrencyFirebase): Currency

}