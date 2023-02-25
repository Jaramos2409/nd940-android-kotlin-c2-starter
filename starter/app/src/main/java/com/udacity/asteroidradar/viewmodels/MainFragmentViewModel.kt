package com.udacity.asteroidradar.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.AsteroidFilter
import com.udacity.asteroidradar.database.getAsteroidDatabase
import com.udacity.asteroidradar.database.getPictureOfDayDatabase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureOfDayRepository
import kotlinx.coroutines.launch
import okio.IOException

class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val _selectedAsteroidFilter = MutableLiveData<AsteroidFilter>()

    private val asteroidsDatabase = getAsteroidDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(asteroidsDatabase)

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.switchMap(_selectedAsteroidFilter) {
            it?.let {
                when (it) {
                    AsteroidFilter.WEEKLY -> asteroidsRepository.fetchWeeklyAsteroidListFromDatabase()
                    AsteroidFilter.TODAY -> asteroidsRepository.fetchTodayAsteroidListFromDatabase()
                    AsteroidFilter.SAVED -> asteroidsRepository.fetchTodayAndNextWeekAsteroidListFromDatabase()
                }
            }
        }

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
        setAsteroidFilterToToday()
        _areThereInternetIssues.value = false
        viewModelScope.launch {
            try {
                pictureOfDayRepository.refreshPictureOfDay()
                asteroidsRepository.fetchAndStoreTodayOfAsteroidsInDatabase()
                asteroidsRepository.fetchAndStoreNextSevenDaysOfAsteroidsInDatabase()
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

    fun setAsteroidFilterToToday() {
        _selectedAsteroidFilter.value = AsteroidFilter.TODAY
        viewModelScope.launch {
            asteroidsRepository.fetchAndStoreTodayOfAsteroidsInDatabase()
        }
    }

    fun setAsteroidFilterToWeekly() {
        _selectedAsteroidFilter.value = AsteroidFilter.WEEKLY
        viewModelScope.launch {
            asteroidsRepository.fetchAndStoreNextSevenDaysOfAsteroidsInDatabase()
        }
    }

    fun setAsteroidFilterToSaved() {
        _selectedAsteroidFilter.value = AsteroidFilter.SAVED
        viewModelScope.launch {
            asteroidsRepository.fetchAndStoreTodayOfAsteroidsInDatabase()
            asteroidsRepository.fetchAndStoreNextSevenDaysOfAsteroidsInDatabase()
        }
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