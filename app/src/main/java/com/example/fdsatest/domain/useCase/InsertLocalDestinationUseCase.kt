package com.example.fdsatest.domain.useCase

import com.example.fdsatest.data.local.LocalDatabase
import com.example.fdsatest.data.local.models.DestinationData
import com.example.fdsatest.utils.WrapperResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityRetainedScoped
class InsertLocalDestinationUseCase @Inject constructor(
    private val localDatabase: LocalDatabase
){
    suspend fun insert(destinationData: DestinationData): WrapperResponse<Unit> {
        val resp = try {
            localDatabase.dao().insert(destinationData)
        }catch (e: Exception){
            return WrapperResponse.Error("Se ha producido un error: ${e.message}")
        }
        return WrapperResponse.Success(resp)
    }
}