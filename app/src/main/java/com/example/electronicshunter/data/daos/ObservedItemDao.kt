package com.example.electronicshunter.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.electronicshunter.data.entities.ObservedItem
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface ObservedItemDao {

    @Insert
    fun save(item: ObservedItem): Completable

    @Query("SELECT * FROM observed_items")
    fun getAll(): Observable<List<ObservedItem>>

    @Query("DELETE FROM observed_items")
    fun deleteAll()
}