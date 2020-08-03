package pl.kontroler.domain.model

import com.google.firebase.Timestamp


/**
 * @author Rafał Nowowieski
 */

class TimestampBuilder {

    private lateinit var dateValue: DateValue

    fun build(): Timestamp = dateValue.timestamp

    fun setDateValue(dateValue: DateValue) {
        this.dateValue = dateValue
    }

    companion object {
        fun builder(): TimestampBuilder = TimestampBuilder()
    }

}