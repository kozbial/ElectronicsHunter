package com.example.electronicshunter.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.electronicshunter.data.ApplicationDatabase.Companion.getDatabase
import com.example.electronicshunter.data.daos.ObservedItemDao
import com.example.electronicshunter.data.entities.ObservedItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.Observable
import io.reactivex.Single

@SuppressLint("CheckResult")
class ObservedItemRepository(context: Context) {
    private var observedItemDao: ObservedItemDao = getDatabase(context).getItemDao()

    fun save(item: ObservedItem){
        observedItemDao.save(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.d("RxJava", "Item saved") },
                { Log.d("RxJava", "Save error") }
            )
    }

    fun getAll(): Observable<List<ObservedItem>> = observedItemDao.getAll()

    fun deleteAll() = observedItemDao.deleteAll()

    fun deleteItemByHref(href: String) = observedItemDao.deleteItemByHref(href)

    fun isItemObserved(href: String) = observedItemDao.countItemsByHref(href)

}