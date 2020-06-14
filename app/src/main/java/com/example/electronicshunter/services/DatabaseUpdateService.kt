package com.example.electronicshunter.services

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.util.Log
import com.example.electronicshunter.data.entities.ObservedItem
import com.example.electronicshunter.data.models.ObservedItemModel
import com.example.electronicshunter.data.repositories.ObservedItemRepository
import com.example.electronicshunter.remote.BackendService
import com.example.electronicshunter.remote.RetrofitClient
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DatabaseUpdateService : JobService(){
    lateinit var observedItemRepository: ObservedItemRepository
    lateinit var context: Context

    companion object {
        const val TAG: String = "DatabaseUpdateService"
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG,"Database update started.")
        performUpdate()
        jobFinished(params, false)
        Log.d(TAG, "Database update finished.")
        return false
    }

    @SuppressLint("CheckResult")
    private fun performUpdate(){
        observedItemRepository = ObservedItemRepository(context)
        observedItemRepository.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                updatePrices(it)
            }, {
                Log.e(TAG,"Failed to get observed items from db.")
            })
    }

    @SuppressLint("CheckResult")
    private fun updatePrices(items: List<ObservedItem>){
        for(item: ObservedItem in items){
            val client: BackendService = RetrofitClient().provideRetrofit().create(BackendService::class.java)
            client.getCurrentItemPrice(item.href)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    observedItemRepository.updateItemPrices(it.price!!, it.href!!)
                }, {
                    Log.e(TAG, "Failed to update item price")
                })
        }
    }
}