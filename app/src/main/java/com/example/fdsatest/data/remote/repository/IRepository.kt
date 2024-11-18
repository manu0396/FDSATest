package com.example.fdsatest.data.remote.repository

import com.example.fdsatest.data.remote.models.Destination


interface IRepository {
    suspend fun getAll(): List<Destination>
    suspend fun deleteById(id: Int)
    suspend fun update(destination: Destination)
    suspend fun create(destination: Destination)
}