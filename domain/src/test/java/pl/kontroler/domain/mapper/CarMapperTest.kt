package pl.kontroler.domain.mapper

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mapstruct.factory.Mappers
import pl.kontroler.domain.model.Car
import pl.kontroler.firebase.model.CarFirebase


/**
 * @author Rafał Nowowieski
 */

class CarMapperTest {

    @Rule
    @JvmField
    val exception: ExpectedException = ExpectedException.none()

    @Test
    fun shouldSuccess_mapToFirebase() {
        val carExpense = Car.create("Name", 123, true)

        val mapper = Mappers.getMapper(CarMapper::class.java)
        val carFirebase = mapper.mapToFirebase(carExpense)

        MatcherAssert.assertThat(carFirebase.name, CoreMatchers.`is`("Name"))
        MatcherAssert.assertThat(carFirebase.counter, CoreMatchers.`is`(123))
        MatcherAssert.assertThat(carFirebase.isCurrent, CoreMatchers.`is`(true))
    }

    @Test
    fun shouldSuccess_mapToModel() {
        val carFirebase = CarFirebase(
            name = "Name",
            counter = 123,
            isCurrent = true
        )

        val mapper = Mappers.getMapper(CarMapper::class.java)
        val car = mapper.mapToModel("1", carFirebase)

        MatcherAssert.assertThat(car.uid, CoreMatchers.`is`("1"))
        MatcherAssert.assertThat(car.name, CoreMatchers.`is`("Name"))
        MatcherAssert.assertThat(car.counter, CoreMatchers.`is`(123))
        MatcherAssert.assertThat(car.isCurrent, CoreMatchers.`is`(true))
    }

}