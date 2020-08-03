package pl.kontroler.carspendsmoney.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.kontroler.carspendsmoney.R
import pl.kontroler.carspendsmoney.databinding.FragmentHomeBinding
import pl.kontroler.carspendsmoney.ui.MainActivity
import pl.kontroler.carspendsmoney.ui.login.LoginViewModel

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val vm: HomeViewModel by viewModel()
    private val loginVM by lazy<LoginViewModel> { requireActivity().getViewModel() }

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.viewmodel = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginVM.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            if (authenticationState == LoginViewModel.AuthenticationState.UNAUTHENTICATED) {
                findNavController().navigate(R.id.login_fragment)
            }
        })

        showBottomNavigationView()
    }

    private fun showBottomNavigationView() {
        (requireActivity() as MainActivity).showBottomNavigationView()
    }

}