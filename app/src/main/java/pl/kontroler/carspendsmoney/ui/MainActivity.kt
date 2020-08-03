package pl.kontroler.carspendsmoney.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.kontroler.carspendsmoney.R
import pl.kontroler.carspendsmoney.databinding.ActivityMainBinding
import pl.kontroler.carspendsmoney.ui.login.LoginViewModel
import pl.kontroler.domain.model.Car
import pl.kontroler.firebase.util.Resource2

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    private val loginVM by lazy<LoginViewModel> { getViewModel() }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private val carsMenuMap = HashMap<Int, Car>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewmodel = vm
        binding.lifecycleOwner = this

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNav.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        observe()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    fun hideBottomNavigationView() {
        binding.bottomNav.visibility = View.GONE
    }

    fun showBottomNavigationView() {
        binding.bottomNav.visibility = View.VISIBLE
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.apply {
            clear()
            carsMenuMap.forEach { entry ->
                add(0, entry.key, Menu.NONE, entry.value.name)
            }
            add(1, Menu.FIRST + carsMenuMap.size, Menu.NONE, "New car")
        }
        MenuCompat.setGroupDividerEnabled(menu, true)
        return super.onPrepareOptionsMenu(menu)
    }

    private fun observe() {
        observeAuthenticationState()
        observeCars()
    }

    private fun observeAuthenticationState() {
        loginVM.authenticationState.observe(this, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> showBottomNavigationView()
                else -> hideBottomNavigationView()
            }
        })
    }

    private fun observeCars() {
        vm.cars.observe(this, Observer {
            if (it is Resource2.Success) {
                setCarsMenuMap(it.data)
                setCurrentCarName(it.data)
            }
        })
    }

    private fun setCarsMenuMap(cars: List<Car>) {
        carsMenuMap.clear()
        var menuId = Menu.FIRST
        cars.forEach { car ->
            carsMenuMap[menuId] = car
            menuId++
        }
    }

    private fun setCurrentCarName(cars: List<Car>) {
        val car = cars.find { it.isCurrent }
        if (car != null) {
            binding.carName.text = car.name
        }
    }

}