package com.example.electronicshunter.data

import android.app.IntentService
import android.content.Intent
import com.example.electronicshunter.data.repositories.ObservedItemRepository
import com.example.electronicshunter.view.ObservedItemModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DatabaseUpdateService : IntentService(DatabaseUpdateService::class.simpleName) {
    lateinit var observedItemRepository: ObservedItemRepository
    override fun onHandleIntent(intent: Intent?) {
        observedItemRepository = ObservedItemRepository(this)
    }
}