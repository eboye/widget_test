package net.nocopypaste.weatherwidgettest

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
    private val prefsFilename = "WeatherSharedPreferences"
    private val sharedCity = "city"
    private val sharedCurrentWeather = "currentWeather"
    private val sharedUnit = "unit"
    private val sharedLat = "lat"
    private val sharedLon = "lon"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, Context.MODE_PRIVATE)

    var city: String?
        get() = prefs.getString(sharedCity, null)
        set(value) = prefs.edit().putString(sharedCity,value).apply()

    var unit: Long
        get() = prefs.getLong(sharedUnit, 0)
        set(value) = prefs.edit().putLong(sharedUnit,value).apply()

    var lat: Long
        get() = prefs.getLong(sharedLat, 0L)
        set(value) = prefs.edit().putString(sharedLat,value.toString()).apply()

    var lon: Long
        get() = prefs.getLong(sharedLon, 0L)
        set(value) = prefs.edit().putString(sharedLon,value.toString()).apply()

    var currentWeather: String?
        get() = prefs.getString(sharedCurrentWeather,"{}")
        set(value) = prefs.edit().putString(sharedCurrentWeather, value).apply()
}