package com.example.fdsatest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fdsatest.data.local.models.DestinationData

@Database(
    entities = [DestinationData::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CustomTypeConverters::class)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun dao(): HotelDAO
}