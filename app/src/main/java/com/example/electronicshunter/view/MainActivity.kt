package com.example.electronicshunter.view

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.*
import androidx.navigation.ui.NavigationUI
import com.example.electronicshunter.R
import com.example.electronicshunter.services.DatabaseUpdateService
import com.example.electronicshunter.services.JobSchedulerService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpBottomNavMenu()
        setJobScheduler(applicationContext)
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
                R.id.observedItemsFragment -> {
                    if(navController.currentDestination?.id == R.id.observedItemsFragment)
                        false
                    else {
                        navController.navigate(R.id.observedItemsFragment, null, bottomNavOptions)
                        true
                    }
                }
                R.id.searchFragment -> {
                    if(navController.currentDestination?.id == R.id.searchFragment)
                        false
                    else{
                        navController.navigate(R.id.searchFragment, null, bottomNavOptions)
                        true
                    }
                }
                R.id.settingsFragment -> {
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

    fun setJobScheduler(context: Context){
        Log.v("JOBSCHEDULER", "Job scheduler is starting")
        val jobScheduler: JobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val serviceName = ComponentName(context, DatabaseUpdateService::class.java)
        val jobInfo: JobInfo
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            jobInfo = JobInfo.Builder(1, serviceName)
                .setPeriodic(900000)
                .build()
        }
        else{
            jobInfo = JobInfo.Builder(1, serviceName)
                .setPeriodic(60000)
                .build()
        }
        jobScheduler.schedule(jobInfo)
    }
}
