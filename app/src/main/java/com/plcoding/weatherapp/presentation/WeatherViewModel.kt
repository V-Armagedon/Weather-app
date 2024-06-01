package com.plcoding.weatherapp.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker,
) : ViewModel() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var state by mutableStateOf(WeatherState())
        private set

    fun loadWeatherInfo(lat: Double?, long: Double?) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            if (lat == null || long == null){
                locationTracker.getCurrentLocation()?.let { location ->
                    latitude = location.latitude
                    longitude = location.longitude
                }
            }else{
                latitude = lat
                longitude = long
            }
            when (val result = repository.getWeatherData(latitude, longitude)) {
                is Resource.Success -> {
                    state = state.copy(
                        weatherInfo = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        weatherInfo = null,
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }
}