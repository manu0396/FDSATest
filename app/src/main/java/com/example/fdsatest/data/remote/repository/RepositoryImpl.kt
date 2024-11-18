package com.example.fdsatest.data.remote.repository

import com.example.fdsatest.data.remote.HotelBediaXApi
import com.example.fdsatest.data.remote.models.Destination

class RepositoryImpl(
    private val hotelBediaXApi: HotelBediaXApi
): IRepository {
    override suspend fun getAll(): List<Destination> {
        return hotelBediaXApi.getAll()
    }

    override suspend fun deleteById(id: Int) {
        return hotelBediaXApi.deleteById(id)
    }

    override suspend fun update(destination: Destination) {
        return hotelBediaXApi.update(destination, destination.Id)
    }

    override suspend fun create(destination: Destination) {
        return hotelBediaXApi.create(destination)
    }
}