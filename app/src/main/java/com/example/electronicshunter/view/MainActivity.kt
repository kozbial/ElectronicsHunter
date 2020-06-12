package com.example.electronicshunter.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.*
import androidx.navigation.ui.NavigationUI
import com.example.electronicshunter.R
import com.example.electronicshunter.data.DatabaseUpdateService
import com.example.electronicshunter.remote.BackendService
import com.example.electronicshunter.remote.RetrofitClient
import com.example.electronicshunter.utils.CustomRecyclerViewAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_item.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, DatabaseUpdateService::class.java)
        startService(intent)
        setUpBottomNavMenu()
    }

    private fun setUpBottomNavMenu(){
        val navController: NavController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(bottom_navigation, navController)
        setUpBottomNavMenuItemListener(navController)
    }

    private fun setUpBottomNavMenuItemListener(navController: NavController){
        val bottomNavOptions: NavOptions = getBottomNavMenuOptions()
        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.observedItemsPage -> {
                    if(navController.currentDestination?.id == R.id.observedItemsFragment)
                        false
                    else{
                        navController.navigate(R.id.observedItemsFragment, null, bottomNavOptions)
                        true
                    }
                }
                R.id.searchingPage -> {
                    if(navController.currentDestination?.id == R.id.searchFragment)
                        false
                    else{
                        navController.navigate(R.id.searchFragment, null, bottomNavOptions)
                        true
                    }
                }
                R.id.settingsPage -> {
                    if(navController.currentDestination?.id == R.id.settingsFragment)
                        false
                    else{
                        navController.navigate(R.id.settingsFragment, null, bottomNavOptions)
                        true
                    }
                }
                else -> false
            }
        }
    }

    private fun getBottomNavMenuOptions(): NavOptions {
        val options: NavOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.bottom_up)
            .setExitAnim(R.anim.bottom_down)
            .build()
        return options
    }
}
