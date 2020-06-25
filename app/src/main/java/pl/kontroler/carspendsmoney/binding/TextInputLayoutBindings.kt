package pl.kontroler.carspendsmoney.binding

import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

object TextInputLayoutBindings {

    @BindingAdapter(value = ["app:validationText", "app:errorMsg"], requireAll = false)
    @JvmStatic
    fun setError(
        view: TextInputLayout,
        validationText: String,
        errorMsg: String
    ) {
        validate(view.editText?.text?.toString(), validationText, view, errorMsg)
        view.editText?.addTextChangedListener { editable ->
            validate(editable?.toString(), validationText, view, errorMsg)
        }
    }

    private fun validate(
        textToValidate: String?,
        validationText: String,
        view: TextInputLayout,
        errorMsg: String
    ) {
        if (textToValidate != validationText) {
            view.error = errorMsg
        } else {
            view.error = null
        }
    }

    @BindingAdapter(value = ["app:errorResId"], requireAll = false)
    @JvmStatic
    fun setError(
        view: TextInputLayout,
        errorResId: Int?
    ) {
        if (errorResId != null) {
            view.error = view.context.getString(errorResId)
        }
    }

}