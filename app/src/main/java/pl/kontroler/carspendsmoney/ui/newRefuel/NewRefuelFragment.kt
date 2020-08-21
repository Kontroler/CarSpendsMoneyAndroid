package pl.kontroler.carspendsmoney.ui.newRefuel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.getViewModel
import pl.kontroler.carspendsmoney.R
import pl.kontroler.carspendsmoney.databinding.FragmentNewRefuelBinding
import pl.kontroler.carspendsmoney.ui.MainActivity
import pl.kontroler.carspendsmoney.utils.showToast

@ExperimentalCoroutinesApi
class NewRefuelFragment : Fragment() {

    companion object {
        fun newInstance() = NewRefuelFragment()
    }

    private val vm by lazy<NewRefuelViewModel> { requireActivity().getViewModel() }
    private lateinit var binding: FragmentNewRefuelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_refuel, container, false)
        binding.viewmodel = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideBottomNavigationView()
        setOnClickListeners()
        observe()
    }

    private fun setOnClickListeners() {
        setOnCancelClickListener()
        setOnDateClickListener()
    }

    private fun setOnDateClickListener() {
        binding.dateButton.setOnClickListener { showDatePickerDialog() }
    }

    private fun setOnCancelClickListener() {
        binding.cancel.setOnClickListener { popBakStack() }
    }

    private fun popBakStack() {
        findNavController().popBackStack()
    }

    private fun hideBottomNavigationView() {
        (requireActivity() as MainActivity).hideBottomNavigationView()
    }

    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment()
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun observe() {
        observeSaveSuccess()
        observeMessageResource()
    }

    private fun observeSaveSuccess() {
        vm.saveSuccess.observe(viewLifecycleOwner, Observer {
            popBakStack()
        })
    }

    private fun observeMessageResource() {
        vm.messageResource.observe(viewLifecycleOwner, Observer { messageResource ->
            messageResource.showToast(requireContext())
        })
    }

}