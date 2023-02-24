package com.udacity.asteroidradar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.udacity.asteroidradar.network.WifiService
import com.udacity.asteroidradar.work.RefreshAsteroidDataWorker
import com.udacity.asteroidradar.work.RefreshPictureOfDayDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        delayedInit()
        setupWifiService()
    }

    private fun delayedInit() = applicationScope.launch {
        setupRecurringWork()
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                setRequiresDeviceIdle(true)
            }.build()

        val repeatingAsteroidListRequest =
            PeriodicWorkRequestBuilder<RefreshAsteroidDataWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        val repeatingPictureOfDayRequest =
            PeriodicWorkRequestBuilder<RefreshPictureOfDayDataWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        WorkManager
            .getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                RefreshAsteroidDataWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingAsteroidListRequest
            )

        WorkManager
            .getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                RefreshPictureOfDayDataWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingPictureOfDayRequest
            )
    }

    private fun setupWifiService() {
        WifiService.instance.initializeWithApplicationContext(this)
    }

}
