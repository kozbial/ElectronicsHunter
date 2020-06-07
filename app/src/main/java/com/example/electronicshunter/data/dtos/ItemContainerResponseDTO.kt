package com.example.electronicshunter.data.dtos

import com.google.gson.annotations.SerializedName

data class ItemContainerResponseDTO (
    @SerializedName("items")
    val items: List<ItemResponseDTO>?
)