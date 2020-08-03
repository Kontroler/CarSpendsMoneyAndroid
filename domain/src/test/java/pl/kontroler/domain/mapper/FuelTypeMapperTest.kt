package pl.kontroler.domain.mapper

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mapstruct.factory.Mappers
import pl.kontroler.domain.model.FuelType


/**
 * @author Rafał Nowowieski
 */

class FuelTypeMapperTest {

    @Test
    fun shouldSuccess_mapToFirebase() {
        val fuelType = FuelType("Gas", "LPG")
        val mapper = Mappers.getMapper(FuelTypeMapper::class.java)
        val fuelTypeCodeFirebase = mapper.mapToFirebase(fuelType)

        assertThat(fuelTypeCodeFirebase.type, CoreMatchers.`is`("Gas"))
        assertThat(fuelTypeCodeFirebase.code, CoreMatchers.`is`("LPG"))
    }

}