package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {

    @Query("select * from databaseasteroid where closeApproachDate >= :dateStart and closeApproachDate <= :dateEnd order by closeApproachDate")
    fun getAsteroidsBetweenDates(
        dateStart: String,
        dateEnd: String
    ): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid where closeApproachDate = :date")
    fun getAsteroidsOfToday(date: String): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg videos: DatabaseAsteroid)

}