@file:Suppress("DEPRECATION")

package com.example.electronicshunter.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.electronicshunter.R
import com.example.electronicshunter.data.entities.ObservedItem
import com.example.electronicshunter.data.repositories.ObservedItemRepository
import com.example.electronicshunter.view.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NotificationService : JobService() {
    lateinit var observedItemRepository: ObservedItemRepository
    lateinit var context: Context

    companion object {
        const val TAG: String = "NotificationService"
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Notifications sending service started.")
        val sharedPreferences = this.getSharedPreferences("notificationsPreferences", 0)
        if(sharedPreferences.getBoolean("notificationsSwitch", false)){
            sendNotifications()
        }
        else{
            Log.d(TAG, "Notifications disabled by user.")
        }
        jobFinished(params,false)
        Log.d(TAG, "Notifications sending service finished.")
        return false
    }

    @SuppressLint("CheckResult")
    private fun sendNotifications(){
        observedItemRepository = ObservedItemRepository(context)
        observedItemRepository.getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                for(item: ObservedItem in it){
                    if(item.price <= item.goalPrice){
                        sendNotification(item.name, "Przedmiot osiągnął oczekiwaną cenę!")
                    }
                }
            }, {
                Log.e(TAG, "Failed to get observed items form db.")
            })
    }

    private fun sendNotification(title: String, task: String){
        val notificationManager= applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel("inducesmile", "inducesmile", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(notificationChannel)
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("observedItemsFragment", "ObservedItemsFragment")
        val pendingIntent = PendingIntent.getActivity(context, 0 ,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification= NotificationCompat.Builder(applicationContext, "inducesmile")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        notification.build().flags = Notification.FLAG_AUTO_CANCEL
        notificationManager.notify(1, notification.build())
    }
}