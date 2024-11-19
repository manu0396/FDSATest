package com.example.fdsatest.domain.useCase

import com.example.fdsatest.data.local.LocalDatabase
import com.example.fdsatest.data.local.models.DestinationData
import com.example.fdsatest.utils.WrapperResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityRetainedScoped
class GetLocalDestinationsUseCase @Inject constructor(
    private val localDatabase: LocalDatabase,
) {
    //TODO: FIX MODELS
    suspend fun getResults(): WrapperResponse<List<DestinationData>> {
        val resp = try {
            localDatabase.dao().getAll()
        }catch (e: Exception){
            return WrapperResponse.Error("Se ha producido un error: ${e.message}")
        }
        return WrapperResponse.Success(resp)
    }
}