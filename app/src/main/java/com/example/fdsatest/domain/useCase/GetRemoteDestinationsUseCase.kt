package com.example.fdsatest.domain.useCase

import com.example.fdsatest.data.remote.models.Destination
import com.example.fdsatest.data.remote.repository.IRepository
import com.example.fdsatest.utils.WrapperResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class GetRemoteDestinationsUseCase @Inject constructor(
    private val repositoryImpl: IRepository
) {

   //TODO: FIX MODELS
   suspend fun getAll():WrapperResponse<List<Destination>>{
        val resp = try {
            repositoryImpl.getAll()
        }catch (e:Exception){
            return WrapperResponse.Error("Se ha producido un error: ${e.message}")
        }
        return WrapperResponse.Success(resp)
    }

    suspend fun deleteById(id: Int): WrapperResponse<Unit>{
        val resp = try {
            repositoryImpl.deleteById(id)
        }catch (e: Exception){
            return WrapperResponse.Error("Se ha producido un error: ${e.message}")
        }
        return WrapperResponse.Success(resp)
    }

    suspend fun create(destination: Destination):WrapperResponse<Unit>{
        val resp = try {
            repositoryImpl.create(destination)
        }catch (e: Exception){
            return WrapperResponse.Error("Se ha producido un error: ${e.message}")
        }
        return WrapperResponse.Success(resp)
    }

    suspend fun update(destination: Destination):WrapperResponse<Unit>{
        val resp = try {
            repositoryImpl.update(destination)
        }catch (e:Exception){
            return WrapperResponse.Error("Se ha producido un error ${e.message}")
        }
        return WrapperResponse.Success(resp)
    }
}