package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabasePictureOfDay::class], version = 1, exportSchema = false)
abstract class PictureOfDayDatabase : RoomDatabase() {
    abstract val pictureOfDayDao: PictureOfDayDao
}

private lateinit var PICTURE_OF_DAY_ROOM_INSTANCE: PictureOfDayDatabase

fun getPictureOfDayDatabase(context: Context): PictureOfDayDatabase {
    synchronized(PictureOfDayDatabase::class.java) {
        if (!::PICTURE_OF_DAY_ROOM_INSTANCE.isInitialized) {
            PICTURE_OF_DAY_ROOM_INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                PictureOfDayDatabase::class.java,
                "pictureofday"
            ).build()
        }
    }

    return PICTURE_OF_DAY_ROOM_INSTANCE
}