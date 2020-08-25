package pl.kontroler.carspendsmoney.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.kontroler.carspendsmoney.R
import pl.kontroler.carspendsmoney.databinding.ActivityMainBinding
import pl.kontroler.carspendsmoney.ui.editCar.EditCarDialogFragment
import pl.kontroler.carspendsmoney.ui.login.LoginViewModel
import pl.kontroler.domain.model.Car
import pl.kontroler.firebase.util.Resource
import timber.log.Timber

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    private val loginVM by lazy<LoginViewModel> { getViewModel() }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private val carsMenuMap = HashMap<Int, Car>()

    private val bottomNavigationViewVisibility = Channel<BottomNavigationViewVisibility>()

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
        collect()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        if (item.groupId == 1 && item.itemId == Menu.FIRST + carsMenuMap.size) {
            val editCarFragment = EditCarDialogFragment.newInstance()
            editCarFragment.show(supportFragmentManager, "dialog")
        } else {
            try {
                val car = carsMenuMap[item.itemId]
                vm.setCurrentCar(car!!)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    fun hideBottomNavigationView() {
        GlobalScope.launch(Dispatchers.Main) {
            bottomNavigationViewVisibility.send(BottomNavigationViewVisibility.GONE)
        }
    }

    fun showBottomNavigationView() {
        GlobalScope.launch(Dispatchers.Main) {
            bottomNavigationViewVisibility.send(BottomNavigationViewVisibility.VISIBLE)
        }
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
        observeCurrentCar()
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
            if (it is Resource.Success) {
                setCarsMenuMap(it.data)
            }
        })
    }

    private fun observeCurrentCar() {
        vm.currentCar.observe(this, Observer {
            if (it is Resource.Success) {
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

    private fun setCurrentCarName(car: Car) {
        binding.carName.text = car.name
    }

    private fun collect() {
        GlobalScope.launch(Dispatchers.Main) {
            bottomNavigationViewVisibility.consumeAsFlow().collect {
                when (it) {
                    BottomNavigationViewVisibility.VISIBLE ->
                        binding.bottomNav.visibility = View.VISIBLE
                    BottomNavigationViewVisibility.GONE ->
                        binding.bottomNav.visibility = View.GONE
                }
            }
        }
    }

    private enum class BottomNavigationViewVisibility {
        VISIBLE, GONE
    }

}