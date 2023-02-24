package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getPictureOfDayDatabase
import com.udacity.asteroidradar.repository.PictureOfDayRepository
import retrofit2.HttpException

class RefreshPictureOfDayDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshPictureOfDayDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getPictureOfDayDatabase(applicationContext)
        val repository = PictureOfDayRepository(database)

        return try {
            repository.refreshPictureOfDay()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}