package pl.kontroler.firebase.util


/**
 * @author Rafał Nowowieski
 */

sealed class Resource2<out T> {
    class Loading<out T> : Resource2<T>()
    data class Success<out T>(val data: T) : Resource2<T>()
    data class Failure<out T>(val throwable: Throwable) : Resource2<T>()
}