package pl.kontroler.domain.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import pl.kontroler.domain.model.Car
import pl.kontroler.firebase.model.CarFirebase


/**
 * @author Rafał Nowowieski
 */

@Mapper
interface CarMapper {

    @Mappings(
        Mapping(target = "isCurrent", source = "current")
    )
    fun mapToFirebase(car: Car): CarFirebase

    @Mappings(
        Mapping(target = "uid", source = "uid"),
        Mapping(target = "name", source = "carFirebase.name"),
        Mapping(target = "counter", source = "carFirebase.counter"),
        Mapping(target = "isCurrent", source = "carFirebase.current")
    )
    fun mapToModel(uid: String, carFirebase: CarFirebase): Car
}