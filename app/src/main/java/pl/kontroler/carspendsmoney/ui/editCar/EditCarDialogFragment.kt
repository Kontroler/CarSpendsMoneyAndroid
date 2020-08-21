package pl.kontroler.carspendsmoney.ui.editCar

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.kontroler.carspendsmoney.R
import pl.kontroler.carspendsmoney.databinding.FragmentEditCarDialogBinding


@ExperimentalCoroutinesApi
class EditCarDialogFragment : DialogFragment() {

    companion object {
        fun newInstance() = EditCarDialogFragment()
    }

    private val vm: EditCarDialogViewModel by viewModel()
    private lateinit var binding: FragmentEditCarDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_car_dialog, container, false)
        binding.viewmodel = vm
        binding.lifecycleOwner = this

        binding.cancel.setOnClickListener { dismiss() }

        vm.isSaved.observe(viewLifecycleOwner, Observer { dismiss() })

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
//        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

}