package pl.kontroler.domain.model


/**
 * @author Rafał Nowowieski
 */

data class MessageResource(
    val type: Type,
    val resId: Int
) {

    enum class Type {
        Info, Success, Warning, Error
    }

}