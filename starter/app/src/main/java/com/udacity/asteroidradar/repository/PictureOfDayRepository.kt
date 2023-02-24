package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.PictureOfDayDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PictureOfDayRepository(private val database: PictureOfDayDatabase) {

    val pictureOfDay: LiveData<PictureOfDay?> =
        Transformations.map(database.pictureOfDayDao.getMostRecentlySavedPictureOfDay()) {
            it?.asDomainModel()
        }

    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            val currentPictureOfDayResponse = Network.nasa.getNasaImageOfTheDayDataAsync().await()

            if (currentPictureOfDayResponse.mediaType == "image") {
                database.pictureOfDayDao.insert(currentPictureOfDayResponse.asDatabaseModel())
            }

        }
    }

}