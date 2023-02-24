package com.udacity.asteroidradar.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getAsteroidDatabase
import com.udacity.asteroidradar.database.getPictureOfDayDatabase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureOfDayRepository
import kotlinx.coroutines.launch
import okio.IOException

class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val asteroidsDatabase = getAsteroidDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(asteroidsDatabase)
    val asteroids = asteroidsRepository.asteroids

    private val pictureOfDayDatabase = getPictureOfDayDatabase(application)
    private val pictureOfDayRepository = PictureOfDayRepository(pictureOfDayDatabase)
    val pictureOfDay = pictureOfDayRepository.pictureOfDay

    private val _navigateToAsteroidDetailsScreen = MutableLiveData<Asteroid?>()
    val navigateToAsteroidDetailsScreen: LiveData<Asteroid?>
        get() = _navigateToAsteroidDetailsScreen

    private val _areThereInternetIssues = MutableLiveData<Boolean>()
    val areThereInternetIssues: LiveData<Boolean>
        get() = _areThereInternetIssues

    init {
        _areThereInternetIssues.value = false
        viewModelScope.launch {
            try {
                pictureOfDayRepository.refreshPictureOfDay()
                asteroidsRepository.refreshAsteroidList()
            } catch (e: IOException) {
                _areThereInternetIssues.value = true
            }
        }
    }

    fun navigateToAsteroidDetailsScreen(asteroid: Asteroid) {
        _navigateToAsteroidDetailsScreen.value = asteroid
    }

    fun navigationToAsteroidDetailsScreenComplete() {
        _navigateToAsteroidDetailsScreen.value = null
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainFragmentViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}