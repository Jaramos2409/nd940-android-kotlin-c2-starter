package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.getDateOfEightDaysFromTodayStringFormatted
import com.udacity.asteroidradar.getDateOfTodayStringFormatted
import com.udacity.asteroidradar.getDateOfTomorrowStringFormatted
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    suspend fun fetchAndStoreTodayOfAsteroidsInDatabase() {
        withContext(Dispatchers.IO) {
            val asteroidList = Network
                .nasa
                .getAsteroidsDataAsync(
                    endDate = getDateOfTodayStringFormatted()
                )
                .await()
            database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
        }
    }

    suspend fun fetchAndStoreNextSevenDaysOfAsteroidsInDatabase() {
        withContext(Dispatchers.IO) {
            val tomorrowDateFormatted = getDateOfTomorrowStringFormatted()
            val eightDaysFromTodayDateFormatted = getDateOfEightDaysFromTodayStringFormatted()

            val asteroidList = Network
                .nasa
                .getAsteroidsDataAsync(
                    startDate = tomorrowDateFormatted,
                    endDate = eightDaysFromTodayDateFormatted
                )
                .await()

            database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
        }
    }

    fun fetchWeeklyAsteroidListFromDatabase(): LiveData<List<Asteroid>> {
        return Transformations
            .map(
                database
                    .asteroidDao
                    .getAsteroidsBetweenDates(
                        dateStart = getDateOfTomorrowStringFormatted(),
                        dateEnd = getDateOfEightDaysFromTodayStringFormatted()
                    )
            ) {
                it.asDomainModel()
            }
    }

    fun fetchTodayAsteroidListFromDatabase(): LiveData<List<Asteroid>> {
        return Transformations
            .map(
                database
                    .asteroidDao
                    .getAsteroidsOfToday(
                        date = getDateOfTodayStringFormatted()
                    )
            ) {
                it.asDomainModel()
            }
    }

    fun fetchTodayAndNextWeekAsteroidListFromDatabase(): LiveData<List<Asteroid>> {
        return Transformations
            .map(
                database
                    .asteroidDao
                    .getAsteroidsBetweenDates(
                        dateStart = getDateOfTodayStringFormatted(),
                        dateEnd = getDateOfEightDaysFromTodayStringFormatted()
                    )
            ) {
                it.asDomainModel()
            }
    }
}