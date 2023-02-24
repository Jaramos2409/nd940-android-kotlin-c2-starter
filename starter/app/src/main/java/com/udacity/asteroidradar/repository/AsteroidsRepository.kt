package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.getSevenDaysFromToday
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.toString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroidList() {
        withContext(Dispatchers.IO) {
            val currentDayFormatted = getSevenDaysFromToday().toString("yyyy-MM-dd")
            val asteroidList = Network.nasa.getAsteroidsDataAsync(currentDayFormatted).await()
            database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
        }
    }

}