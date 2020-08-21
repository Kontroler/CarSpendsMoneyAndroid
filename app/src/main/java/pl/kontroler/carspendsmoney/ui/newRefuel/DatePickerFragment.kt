package pl.kontroler.carspendsmoney.ui.newRefuel

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.getViewModel
import pl.kontroler.domain.model.DateValue


/**
 * @author Rafał Nowowieski
 */

@ExperimentalCoroutinesApi
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val vm by lazy<NewRefuelViewModel> { requireActivity().getViewModel() }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = DateValue.now()
        val year = date.year
        val month = date.month
        val day = date.day

        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        vm.date.value = DateValue.of(year, month, day)
    }

}