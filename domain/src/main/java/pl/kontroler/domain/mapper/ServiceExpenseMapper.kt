package pl.kontroler.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import pl.kontroler.domain.model.ServiceExpense
import pl.kontroler.firebase.model.ServiceExpenseFirebase


/**
 * @author Rafał Nowowieski
 */

@Mapper(uses = [DateValueMapper::class])
interface ServiceExpenseMapper {

    fun mapToFirebase(serviceExpense: ServiceExpense): ServiceExpenseFirebase

    @Mappings(
        Mapping(target = "uid", source = "uid"),
        Mapping(
            target = "date",
            source = "serviceExpenseFirebase.date"
        ),
        Mapping(target = "description", source = "serviceExpenseFirebase.description"),
        Mapping(target = "totalPrice", source = "serviceExpenseFirebase.totalPrice"),
        Mapping(target = "currency", source = "serviceExpenseFirebase.currency"),
        Mapping(target = "counter", source = "serviceExpenseFirebase.counter")
    )
    fun mapToModel(uid: String, serviceExpenseFirebase: ServiceExpenseFirebase): ServiceExpense

}