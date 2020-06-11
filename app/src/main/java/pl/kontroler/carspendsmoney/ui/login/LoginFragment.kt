package pl.kontroler.carspendsmoney.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.kontroler.carspendsmoney.R
import pl.kontroler.carspendsmoney.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val vm: LoginViewModel by viewModel()
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

}