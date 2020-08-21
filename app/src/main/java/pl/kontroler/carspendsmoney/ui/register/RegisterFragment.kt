package pl.kontroler.carspendsmoney.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.kontroler.carspendsmoney.R
import pl.kontroler.carspendsmoney.databinding.FragmentRegisterBinding
import pl.kontroler.carspendsmoney.utils.showToast

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private val vm: RegisterViewModel by viewModel()
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.viewmodel = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe()
    }

    private fun observe() {
        observeOnUserCreated()
        observeOnErrorResId()
    }

    private fun observeOnUserCreated() {
        vm.userCreated.observe(viewLifecycleOwner, Observer { _ ->
            findNavController().popBackStack()
        })
    }

    private fun observeOnErrorResId() {
        vm.messageResource.observe(viewLifecycleOwner, Observer { messageResource ->
           messageResource.showToast(requireContext())
        })
    }

}