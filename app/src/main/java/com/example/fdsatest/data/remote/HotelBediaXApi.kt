package com.example.fdsatest.data.remote

import com.example.fdsatest.data.remote.models.Destination
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HotelBediaXApi{

    @GET("/api/destinations")
    suspend fun getAll(): List<Destination>

    @GET("/api/destination/{id}")
    suspend fun deleteById(
        @Path("id") id: Int
    ): Int //Int represent 0 or 1

    @POST("/api/destinations")
    suspend fun create(
        @Body destination: Destination
    ): Int //Int represent 0 or 1

    @POST("/api/destination/{id}")
    suspend fun update(
        @Body destination: Destination,
        @Path("id") id: Int
    ): Int //Int represent 0 or 1
}