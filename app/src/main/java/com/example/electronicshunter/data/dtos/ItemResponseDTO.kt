package com.example.electronicshunter.data.dtos

import com.google.gson.annotations.SerializedName

data class ItemResponseDTO (
    @SerializedName("shop_name")
    var shopName: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("price")
    var price: Double?,
    @SerializedName("max_price")
    var max_price: Double?,
    @SerializedName("min_price")
    var min_price: Double?,
    @SerializedName("href")
    var href: String?
)