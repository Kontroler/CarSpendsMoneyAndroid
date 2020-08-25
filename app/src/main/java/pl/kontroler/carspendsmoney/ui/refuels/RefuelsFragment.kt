package pl.kontroler.carspendsmoney.ui.refuels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.kontroler.carspendsmoney.R
import pl.kontroler.carspendsmoney.databinding.FragmentRefuelsBinding
import pl.kontroler.carspendsmoney.ui.MainActivity
import pl.kontroler.carspendsmoney.utils.showToast
import pl.kontroler.firebase.util.Resource

@ExperimentalCoroutinesApi
class RefuelsFragment : Fragment() {

    companion object {
        fun newInstance() = RefuelsFragment()
    }

    private val vm: RefuelsViewModel by viewModel()
    private lateinit var binding: FragmentRefuelsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_refuels, container, false)
        binding.viewmodel = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setOnClickListeners()
        showBottomNavigationView()
        observe()
    }

    private fun setOnClickListeners() {
        setOnAddRefuelClickListener()
    }

    private fun setOnAddRefuelClickListener() {
        binding.addRefuel.setOnClickListener { navigateToNewRefuelFragment() }
    }

    private fun navigateToNewRefuelFragment() {
        findNavController().navigate(R.id.new_refuel_fragment)
    }

    private fun showBottomNavigationView() {
        (requireActivity() as MainActivity).showBottomNavigationView()
    }

    private fun observe() {
        observeAllFuelExpenseList()
    }

    private fun observeAllFuelExpenseList() {
        vm.fuelExpenses.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error loading data", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun observeOnMessageResource() {
        vm.messageResource.observe(viewLifecycleOwner, {
            it.showToast(requireContext())
        })
    }

}