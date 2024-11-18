package com.example.fdsatest.domain.useCase

import com.example.fdsatest.data.remote.models.Destination
import com.example.fdsatest.data.remote.HotelBediaXApi
import com.example.fdsatest.utils.WrapperResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityRetainedScoped
class GetRemoteDestinationsUseCase @Inject constructor(
    private val hotelBediaXApi: HotelBediaXApi
) {

   suspend fun getAll():WrapperResponse<List<Destination>>{
        val resp = try {
            hotelBediaXApi.getAll()
        }catch (e:Exception){
            return WrapperResponse.Error("Se ha producido un error: ${e.message}")
        }
        return WrapperResponse.Success(resp)
    }

    suspend fun deleteById(id: Int): WrapperResponse<Int>{
        val resp = try {
            hotelBediaXApi.deleteById(id)
        }catch (e: Exception){
            return WrapperResponse.Error("Se ha producido un error: ${e.message}")
        }
        return WrapperResponse.Success(resp)
    }

    suspend fun create(destination: Destination):WrapperResponse<Int>{
        val resp = try {
            hotelBediaXApi.create(destination)
        }catch (e: Exception){
            return WrapperResponse.Error("Se ha producido un error: ${e.message}")
        }
        return WrapperResponse.Success(resp)
    }

    suspend fun update(destination: Destination):WrapperResponse<Int>{
        val resp = try {
            hotelBediaXApi.update(destination, destination.Id)
        }catch (e:Exception){
            return WrapperResponse.Error("Se ha producido un error ${e.message}")
        }
        return WrapperResponse.Success(resp)
    }
}