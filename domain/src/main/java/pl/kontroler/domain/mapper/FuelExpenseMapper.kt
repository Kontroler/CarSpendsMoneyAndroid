package pl.kontroler.domain.mapper

import com.google.firebase.Timestamp
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import pl.kontroler.domain.model.DateValue
import pl.kontroler.domain.model.FuelExpense
import pl.kontroler.firebase.model.FuelExpenseFirebase


/**
 * @author Rafał Nowowieski
 */


@Mapper(uses = [FuelTypeMapper::class, DateValueMapper::class])
interface FuelExpenseMapper {

    @Mapping(target = "date", source = "date")
    fun mapToFirebase(fuelExpense: FuelExpense): FuelExpenseFirebase

    @Mappings(
        Mapping(target = "uid", source = "uid"),
        Mapping(
            target = "date",
            source = "fuelExpenseFirebase.date"
        ),
        Mapping(target = "description", source = "fuelExpenseFirebase.description"),
        Mapping(target = "totalPrice", source = "fuelExpenseFirebase.totalPrice"),
        Mapping(target = "quantity", source = "fuelExpenseFirebase.quantity"),
        Mapping(target = "unit", source = "fuelExpenseFirebase.unit"),
        Mapping(target = "unitPrice", source = "fuelExpenseFirebase.unitPrice"),
        Mapping(target = "currency", source = "fuelExpenseFirebase.currency"),
        Mapping(target = "counter", source = "fuelExpenseFirebase.counter"),
        Mapping(target = "fuelType", source = "fuelExpenseFirebase.fuelType")
    )
    fun mapToModel(uid: String, fuelExpenseFirebase: FuelExpenseFirebase): FuelExpense

    fun timestampToDateValue(timestamp: Timestamp): DateValue = DateValue(timestamp)
}
