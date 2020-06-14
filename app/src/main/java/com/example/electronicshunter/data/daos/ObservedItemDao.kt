package com.example.electronicshunter.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.electronicshunter.data.entities.ObservedItem
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ObservedItemDao {

    @Insert
    fun save(item: ObservedItem): Completable

    @Query("SELECT * FROM observed_items")
    fun getAll(): Maybe<List<ObservedItem>>

    @Query("DELETE FROM observed_items")
    fun deleteAll()

    @Query("DELETE FROM observed_items WHERE href = :href")
    fun deleteItemByHref(href: String)

    @Query("SELECT COUNT(*) FROM observed_items WHERE href = :href")
    fun countItemsByHref(href: String): Int

    @Query("UPDATE observed_items SET maxPrice = CASE WHEN maxPrice < :price THEN :price ELSE maxPrice end, minPrice = CASE WHEN minPrice > :price THEN :price ELSE minPrice end, price = CASE WHEN price != :price THEN :price ELSE price end WHERE href = :href")
    fun updateItemPrices(price: Double, href: String)
}