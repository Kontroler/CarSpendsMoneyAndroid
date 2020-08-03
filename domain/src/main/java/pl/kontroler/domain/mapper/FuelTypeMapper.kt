package pl.kontroler.domain.mapper

import org.mapstruct.Mapper
import pl.kontroler.domain.model.FuelType
import pl.kontroler.firebase.model.FuelTypeCodeFirebase
import pl.kontroler.firebase.model.FuelTypeFirebase


/**
 * @author Rafał Nowowieski
 */

@Mapper
interface FuelTypeMapper {

    fun mapToModel(fuelTypeFirebase: FuelTypeFirebase): FuelType

    fun mapToFirebase(fuelType: FuelType): FuelTypeCodeFirebase
}