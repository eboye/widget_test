package net.nocopypaste.weatherwidgettest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import heyalex.widgethelper.WidgetUpdater
import okhttp3.*
import java.io.IOException
import java.net.URL

class SmallWidgetUpdater : WidgetUpdater() {
    override fun update(context: Context, dataBundle: Bundle?, vararg ids: Int) {
        //This callback will be running on background thread
        val appWidgetManager = AppWidgetManager.getInstance(context)
        var weatherData: WeatherMapper? = null
        try {
            //DB or Internet requests
//            Thread.sleep(10000)
            getWeather(context, getCity(context), getUnit(context))
            Thread.sleep(1000) // wait for the data to be written to sharedPref
            weatherData = WeatherMapper.fromJson(getWeatherData(context))
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
            .let { intent ->
                PendingIntent.getActivity(context, 0, intent, 0)
            }

        //make RemoteViews depends on dataBundle and update by widget ID
        val remoteViews = RemoteViews(context.packageName, R.layout.current_weather).apply {}
        remoteViews.setOnClickPendingIntent(R.id.city, pendingIntent)
        if (weatherData?.weather != null){
            remoteViews.setTextViewText(R.id.condition, weatherData.weather!![0].description)
            remoteViews.setImageViewResource(R.id.icon, context.resources.getIdentifier("ic_01d", "drawable", context.packageName))
            remoteViews.setTextViewText(R.id.city, weatherData.name)
            remoteViews.setTextViewText(R.id.temperature, weatherData.main?.temp.toString().substringBefore(".") + getUnitString(context))
        }
        for (widgetId in ids) {
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun makeNotification(context: Context): Notification {
        val notifChannelID = "net.nocopypaste.weatherwidgettest.widget_updater"
        val channelName = "Weather widget updating"
        val chan = NotificationChannel(notifChannelID, channelName, NotificationManager.IMPORTANCE_NONE)
        val manager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chan)

        val notificationBuilder = Notification.Builder(context, notifChannelID)
        return notificationBuilder.build()
    }

    companion object {

        private fun getCity(context: Context): String {
            val prefs: Prefs?
            prefs = Prefs(context)
            return prefs.city!!
        }

        private fun getWeatherData(context: Context): String {
            val prefs: Prefs?
            prefs = Prefs(context)
            return prefs.currentWeather!!
        }

        private fun getLat(context: Context): Long {
            val prefs: Prefs?
            prefs = Prefs(context)
            return prefs.lat
        }

        private fun getLon(context: Context): Long {
            val prefs: Prefs?
            prefs = Prefs(context)
            return prefs.lon
        }

        private fun getUnitString(context: Context): String {
            val prefs: Prefs?
            prefs = Prefs(context)
            val unit = prefs.unit
            val unitString: String
            unitString = when (unit) {
                0.toLong() -> {
                    "°C"
                }
                1.toLong() -> {
                    "°F"
                }
                else -> {
                    "K"
                }
            }
            return unitString
        }

        private fun getUnit(context: Context): String {
            val prefs: Prefs?
            prefs = Prefs(context)
            val unit = prefs.unit
            val unitString: String
            unitString = when (unit) {
                0.toLong() -> {
                    "metric"
                }
                1.toLong() -> {
                    "imperial"
                }
                else -> {
                    ""
                }
            }
            return unitString
        }

        private fun getWeather(context: Context, city: String, unitString: String) {
            val owmID = ""
            val url: URL = if (getCity(context) != "") {
                URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=$unitString&appId=$owmID")
            } else {
                URL("https://api.openweathermap.org/data/2.5/weather?lat=${getLat(context)}&lon=${getLon(context)}&units=$unitString&appId=$owmID")
            }
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        val responseBody = response.body
                        val content = responseBody!!.string()

                        val prefs: Prefs?
                        prefs = Prefs(context)
                        prefs.currentWeather = content
                    }
                }
            })
        }
    }
}