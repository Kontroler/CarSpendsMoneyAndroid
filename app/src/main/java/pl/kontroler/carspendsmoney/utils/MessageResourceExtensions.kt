package pl.kontroler.carspendsmoney.utils

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty
import pl.kontroler.domain.model.MessageResource


/**
 * @author Rafał Nowowieski
 */

fun MessageResource.showToast(context: Context) {
    when (type) {
        MessageResource.Type.Info -> Toasty.info(
            context,
            context.getString(resId),
            Toast.LENGTH_LONG,
            true
        ).show()
        MessageResource.Type.Warning -> Toasty.warning(
            context,
            context.getString(resId),
            Toast.LENGTH_LONG,
            true
        ).show()
        MessageResource.Type.Error -> Toasty.error(
            context,
            context.getString(resId),
            Toast.LENGTH_LONG,
            true
        ).show()
        MessageResource.Type.Success -> Toasty.success(
            context,
            context.getString(resId),
            Toast.LENGTH_LONG,
            true
        ).show()
    }
}