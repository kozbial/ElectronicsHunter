package com.example.electronicshunter.data.dtos

import com.google.gson.annotations.SerializedName

data class ItemResponseDTO (
    @SerializedName("shopName")
    var shopName: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("price")
    var price: Double?,
    @SerializedName("maxPrice")
    var max_price: Double?,
    @SerializedName("minPrice")
    var min_price: Double?,
    @SerializedName("href")
    var href: String?,
    @SerializedName("imageHref")
    var image_href: String?
)