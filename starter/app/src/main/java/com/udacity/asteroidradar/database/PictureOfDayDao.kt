package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PictureOfDayDao {

    @Query("select * from databasepictureofday order by date desc limit 1")
    fun getMostRecentlySavedPictureOfDay(): LiveData<DatabasePictureOfDay>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(databasePictureOfDay: DatabasePictureOfDay)

}