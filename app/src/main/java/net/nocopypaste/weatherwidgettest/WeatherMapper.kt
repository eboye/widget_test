// To parse the JSON, install Klaxon and do:
//
//   val weatherMapper = WeatherMapper.fromJson(jsonString)

package net.nocopypaste.weatherwidgettest

import com.beust.klaxon.*

private val klaxon = Klaxon()

data class WeatherMapper (
        val coord: Coord? = null,
        val weather: List<Weather>? = null,
        val base: String? = null,
        val main: Main? = null,
        val visibility: Long? = null,
        val wind: Wind? = null,
        val clouds: Clouds? = null,
        val dt: Long? = null,
        val sys: Sys? = null,
        val timezone: Long? = null,
        val id: Long? = null,
        val name: String? = null,
        val cod: Long? = null
) {
    fun toJson() = klaxon.toJsonString(this)

    companion object {
        fun fromJson(json: String) = klaxon.parse<WeatherMapper>(json)
    }
}

data class Clouds (
        val all: Long? = null
)

data class Coord (
        val lon: Double? = null,
        val lat: Double? = null
)

data class Main (
        val temp: Double? = null,

        @Json(name = "feels_like")
        val feelsLike: Double? = null,

        @Json(name = "temp_min")
        val tempMin: Double? = null,

        @Json(name = "temp_max")
        val tempMax: Double? = null,

        val pressure: Long? = null,
        val humidity: Long? = null
)

data class Sys (
        val type: Long? = null,
        val id: Long? = null,
        val country: String? = null,
        val sunrise: Long? = null,
        val sunset: Long? = null
)

data class Weather (
        val id: Long? = null,
        val main: String? = null,
        val description: String? = null,
        val icon: String? = null
)

data class Wind (
        val speed: Double? = null,
        val deg: Long? = null
)
