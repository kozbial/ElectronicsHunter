package com.example.electronicshunter.remote

import com.example.electronicshunter.data.dtos.ItemContainerResponseDTO
import com.example.electronicshunter.data.dtos.ItemResponseDTO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface BackendService {

    @GET("all")
    fun getAllItems(): Single<ItemContainerResponseDTO>

    @GET("name/{name}")
    fun getItemsByName(@Path("name")name: String): Single<List<ItemResponseDTO>>
}