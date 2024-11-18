package com.example.fdsatest.di

import com.example.fdsatest.BuildConfig
import com.example.fdsatest.data.remote.ApiClient
import com.example.fdsatest.data.remote.HotelBediaXApi
import com.example.fdsatest.data.remote.repository.IRepository
import com.example.fdsatest.data.remote.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    private val httpClient = OkHttpClient.Builder()

    @Singleton
    @Provides
    fun provideRepositoryImpl(): IRepository {
        return RepositoryImpl(
            provideApi()
        )
    }

    @Singleton
    @Provides
    fun provideApi(): HotelBediaXApi {
        httpClient.addInterceptor(getInterceptor())
        return ApiClient(
            baseUrl = BuildConfig.API_URL,
            converterFactory = GsonConverterFactory.create(),
            okHttpClientBuilder = httpClient
        ).createService(HotelBediaXApi::class.java)
    }

    private fun getInterceptor(): Interceptor {
        try {
            return Interceptor {
                val request = it.request()
                it.proceed(
                    request.newBuilder()
                        .header("Accept", "application/json")
                        .build()
                )
            }
        } catch (exception: Exception) {
            throw Exception(exception.message)
        }
    }
}