package com.example.electronicshunter.data

import android.app.IntentService
import android.content.Intent
import com.example.electronicshunter.data.repositories.ObservedItemRepository

class DatabaseUpdateService : IntentService(DatabaseUpdateService::class.simpleName) {
    lateinit var observedItemRepository: ObservedItemRepository
    override fun onHandleIntent(intent: Intent?) {
        observedItemRepository = ObservedItemRepository(this)
    }
}