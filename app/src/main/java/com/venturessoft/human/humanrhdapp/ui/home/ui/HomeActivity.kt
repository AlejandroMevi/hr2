package com.venturessoft.human.humanrhdapp.ui.home.ui
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.databinding.ActivityHomeBinding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.vm.TimeManagementVM


class HomeActivity : AppCompatActivity(), MainInterface {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val timeManagementVM: TimeManagementVM by viewModels()
    private val fromBottom: android.view.animation.Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: android.view.animation.Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }
    private val fromTop: android.view.animation.Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_top_anim) }
    private val toTop: android.view.animation.Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_top_anim) }
    private var navHostFragment = NavHostFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        getCatalogs()
        initToolbar()
        initializeView()
        setContentView(binding.root)
        showButtonBar.observe(this){
            showButtonBar(it)
        }
    }
    private fun getCatalogs(){
        timeManagementVM.getZona()
        timeManagementVM.getSupervisor()
        timeManagementVM.getRolTurn()
        timeManagementVM.getReasons()
        timeManagementVM.getCalendar()
        timeManagementVM.getDepartment()
        timeManagementVM.getCategory()
        timeManagementVM.getConcepto()
        timeManagementVM.codid()
        timeManagementVM.catalogoPermisos()
    }
    companion object {
        var positionVm = MutableLiveData(0)
        var showButtonBar = MutableLiveData(true)
    }
    private fun initToolbar() {
        binding.tvTitle.isSelected = true
        positionVm.value = 0
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setSupportActionBar(binding.toolBar)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.mainFragment))
        binding.toolBar.setupWithNavController(navController, appBarConfiguration)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
    override fun setTextToolbar(text: String) {
        binding.tvTitle.text = text
    }
    private fun showButtonBar(isVisible: Boolean) {
        if(isVisible != binding.bottomNavigationView.isVisible ){
            if (isVisible){
                binding.bottomNavigationView.startAnimation(fromBottom)
            }else{
                binding.bottomNavigationView.startAnimation(toBottom)
            }
            binding.bottomNavigationView.isVisible = isVisible
        }
    }
    override fun showLottie(isVisible: Boolean) {
        if (isVisible) {
            binding.toolBar.startAnimation(fromTop)
            binding.bottomNavigationView.startAnimation(fromBottom)
        } else {
            binding.toolBar.startAnimation(toTop)
            binding.bottomNavigationView.startAnimation(toBottom)
        }
        binding.toolBar.isVisible = isVisible
        binding.bottomNavigationView.isVisible = isVisible
    }
    private fun initializeView() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            if (binding.bottomNavigationView.selectedItemId != it.itemId) {
                when (it.itemId) {
                    R.id.home -> {
                        positionVm.value = 0
                    }
                    R.id.menu -> {
                        positionVm.value = 1
                    }
                }
            }
            if (navHostFragment.childFragmentManager.backStackEntryCount != 0) {
                findNavController(R.id.nav_host_fragment).navigate(R.id.to_mainFragment)
                findNavController(R.id.nav_host_fragment).popBackStack()
            }
            true
        }
    }
    override fun showLoading(isShowing: Boolean) {
        binding.progress.root.isVisible = isShowing
        if (isShowing) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }
}