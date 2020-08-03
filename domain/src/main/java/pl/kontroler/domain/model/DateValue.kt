package pl.kontroler.domain.model

import com.github.pozo.KotlinBuilder
import com.google.firebase.Timestamp
import org.threeten.bp.*


/**
 * @author Rafał Nowowieski
 */

@KotlinBuilder
data class DateValue constructor(val timestamp: Timestamp) {

    val value
        get() = Instant.ofEpochMilli(timestamp.toDate().time)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

    val year: Int get() = value.year
    val month: Int get() = value.monthValue
    val day: Int get() = value.dayOfMonth

    val second: Int get() = LocalDateTime.of(year, month, day, 0, 0, 0).second
    val nano: Int get() = LocalDateTime.of(year, month, day, 0, 0, 0).nano

    override fun toString(): String {
        return value.toString()
    }

    override fun equals(other: Any?): Boolean {
        return other is DateValue && other.value == value
    }

    override fun hashCode(): Int {
        return timestamp.hashCode()
    }

    companion object {
        fun of(year: Int, month: Int, day: Int): DateValue {
            val localDateTime = LocalDateTime.of(year, month, day, 0, 0, 0)
            return localDateTimeToTimestamp(localDateTime)
        }

        fun now(): DateValue {
            val localDateTime = LocalDate.now().atStartOfDay()
            return localDateTimeToTimestamp(localDateTime)
        }

        private fun localDateTimeToTimestamp(localDateTime: LocalDateTime): DateValue {
            val date =
                DateTimeUtils.toDate(localDateTime.atZone(ZoneOffset.systemDefault()).toInstant())
            return DateValue(Timestamp(date))
        }

    }
}