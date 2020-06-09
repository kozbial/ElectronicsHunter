package com.example.electronicshunter.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "observed_items")
data class ObservedItem(
    var name: String,
    var shopName: String,
    var price: Double,
    var minPrice: Double,
    var maxPrice: Double,
    var href: String,
    var goalPrice: Double
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
