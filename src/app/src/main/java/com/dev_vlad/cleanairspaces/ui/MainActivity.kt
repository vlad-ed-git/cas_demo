package com.dev_vlad.cleanairspaces.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.Glide
import com.dev_vlad.cleanairspaces.R
import com.dev_vlad.cleanairspaces.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()
        //specify home fragments
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mapFragment,
            )
        )
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupActionBarWithNavController(navController, appBarConfiguration)

        //hide and show menus depending on fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.mapFragment -> {
                    binding.apply {
                        toolbar.isVisible = true
                        toolbarLogo.isVisible = true
                        Glide.with(this@MainActivity)
                            .load(R.drawable.clean_air_spaces_logo_name)
                            .into(toolbarLogo)
                    }
                }

                else -> {
                    binding.apply {
                        toolbar.isVisible = false
                    }
                }

            }
        }
    }

    //toolbar handle back navigation
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}