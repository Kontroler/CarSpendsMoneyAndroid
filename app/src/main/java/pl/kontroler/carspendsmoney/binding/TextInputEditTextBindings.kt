package pl.kontroler.carspendsmoney.binding

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.google.android.material.textfield.TextInputEditText


object TextInputEditTextBindings {

    @BindingAdapter("android:text")
    @JvmStatic
    fun setText(view: TextInputEditText, value: Int?) {
        if (view.text != null && view.text.toString().isNotEmpty()
            && view.text.toString().toInt() != value
        ) {
            view.setText(value.toString())
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    @JvmStatic
    fun getText(view: TextInputEditText): Int {
        return view.text.toString().toInt()
    }

}