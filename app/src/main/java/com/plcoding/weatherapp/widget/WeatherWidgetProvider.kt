package com.plcoding.weatherapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.plcoding.weatherapp.R
import com.plcoding.weatherapp.presentation.MainActivity
import com.plcoding.weatherapp.data.repository.WeatherRepositoryImpl
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.util.Resource
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WeatherWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var locationTracker: LocationTracker

    @Inject
    lateinit var weatherRepository: WeatherRepositoryImpl

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_weather)

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.widTemperature, pendingIntent)
        views.setOnClickPendingIntent(R.id.widWeatherDesc, pendingIntent)

        CoroutineScope(Dispatchers.IO).launch {
            val location = locationTracker.getCurrentLocation()
            if (location != null) {
                when (val result = weatherRepository.getWeatherData(location.latitude, location.longitude)) {
                    is Resource.Success -> {
                        val weatherInfo = result.data
                        weatherInfo?.let { updateWeatherViews(views, it) }
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                    is Resource.Error -> {
                        views.setTextViewText(R.id.widWeatherDesc, "Error")
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                }
            } else {
                views.setTextViewText(R.id.widWeatherDesc, "Location unavailable")
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
    private fun updateWeatherViews(views: RemoteViews, weatherInfo: WeatherInfo) {
        val currentWeather = weatherInfo.currentWeatherData
        currentWeather?.let {
            views.setTextViewText(R.id.widTemperature, "${it.temperatureCelsius}°C")
            views.setTextViewText(R.id.widWeatherDesc, it.weatherType.weatherDesc)
        }
    }
}