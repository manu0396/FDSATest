package com.example.fdsatest.di

import android.content.Context
import androidx.room.Room
import com.example.fdsatest.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.fdsatest.data.local.HotelDAO
import com.example.fdsatest.data.local.LocalDatabase
import com.example.fdsatest.domain.useCase.GetLocalDestinationsUseCase
import com.example.fdsatest.domain.useCase.GetResultByIdUseCase
import com.example.fdsatest.domain.useCase.InsertLocalDestinationUseCase
import com.example.fdsatest.domain.useCase.RemoveLocalDestinationUseCase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalModule {
    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LocalDatabase::class.java,
            BuildConfig.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(localDatabase: LocalDatabase): HotelDAO {
        return localDatabase.dao()
    }

    @Provides
    @Singleton
    fun provideLocalDatabaseUseCase(@ApplicationContext context: Context): GetLocalDestinationsUseCase {
        return GetLocalDestinationsUseCase(provideDB(context))
    }

    @Provides
    @Singleton
    fun provideGetResultByIdUseCase(@ApplicationContext context: Context): GetResultByIdUseCase {
        return GetResultByIdUseCase(provideDB(context))
    }

    @Provides
    @Singleton
    fun provideInsertLocalDestinationUseCase(@ApplicationContext context: Context): InsertLocalDestinationUseCase {
        return InsertLocalDestinationUseCase(provideDB(context))
    }

    @Provides
    @Singleton
    fun provideRemoveLocalDestinationUseCase(@ApplicationContext context: Context): RemoveLocalDestinationUseCase {
        return RemoveLocalDestinationUseCase(provideDB(context))
    }
}