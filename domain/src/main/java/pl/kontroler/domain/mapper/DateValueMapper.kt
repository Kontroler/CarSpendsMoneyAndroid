package pl.kontroler.domain.mapper

import com.google.firebase.Timestamp
import org.mapstruct.Mapper
import pl.kontroler.domain.model.DateValue
import pl.kontroler.domain.model.TimestampBuilder


/**
 * @author Rafał Nowowieski
 */

@Mapper
abstract class DateValueMapper {

    abstract fun mapToTimestampBuilder(dateValue: DateValue): TimestampBuilder

    fun mapToTimestamp(dateValue: DateValue): Timestamp {
        val builder = TimestampBuilder.builder()
        builder.setDateValue(dateValue)
        return builder.build()
    }

}