package com.example.teamup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)!! as NavHostFragment
        navController = navHostFragment.navController
        navController.navigate(R.id.loginFragment)


        val appBarConfiguration = AppBarConfiguration(setOf(R.id.loginFragment, R.id.signupFragment, R.id.loginFragment))
        val topAppBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar)
        setSupportActionBar(topAppBar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        topAppBar.setNavigationOnClickListener {
            navController.popBackStack()
        }
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.eventsListFragment -> {
                    navController.navigate(R.id.eventsListFragment)
                    true
                }
                R.id.findingPeopleFragment -> {
                    navController.navigate(R.id.findingPeopleFragment)
                    true
                }
                R.id.myTeamsFragment -> {
                    navController.navigate(R.id.myTeamsFragment)
                    true
                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                R.id.myEventsFragment -> {
                    navController.navigate(R.id.myEventsFragment)
                    true
                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.loginFragment, R.id.signupFragment -> {
                    topAppBar.visibility = View.GONE
                    bottomNavigationView.visibility = View.GONE
                }

                else -> {
                    topAppBar.visibility = View.VISIBLE
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_nav_menu, menu)
        return true
    }
    companion object {
        const val role = 0
    }
}