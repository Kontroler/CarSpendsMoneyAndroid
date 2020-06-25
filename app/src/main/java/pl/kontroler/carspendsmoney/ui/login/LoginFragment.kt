package pl.kontroler.carspendsmoney.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.play.core.internal.v
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.kontroler.carspendsmoney.R
import pl.kontroler.carspendsmoney.databinding.FragmentLoginBinding
import timber.log.Timber

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val vm by lazy<LoginViewModel> { requireActivity().getViewModel() }
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.viewmodel = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnRegisterButtonClickListener()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            vm.refuseAuthentication()
            findNavController().popBackStack(R.id.home_fragment, false)
        }

        observe()
    }

    private fun setOnRegisterButtonClickListener() {
        binding.register.setOnClickListener {
            findNavController().navigate(R.id.register_fragment)
        }
    }

    private fun observe() {
        observeOnUserSignIn()
    }

    private fun observeOnUserSignIn() {
        vm.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> findNavController().popBackStack()
                LoginViewModel.AuthenticationState.INVALID_AUTHENTICATION -> println("error")
                else -> Timber.e("Error")
            }
        })
    }

}