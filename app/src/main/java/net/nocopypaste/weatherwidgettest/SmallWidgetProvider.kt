package net.nocopypaste.weatherwidgettest

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import heyalex.widgethelper.RemoteViewsUpdater
import heyalex.widgethelper.WidgetUpdateService

@RemoteViewsUpdater(SmallWidgetUpdater::class)
class SmallWidgetProvider : AppWidgetProvider() {

    override fun onReceive(context: Context?, intent: Intent?) {
        println("updateWidgets onReceive")
        super.onReceive(context, intent)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetIds = appWidgetManager.getAppWidgetIds(
            context?.let { ComponentName(it, SmallWidgetProvider::class.java) })
        // update widgets
        for (widgetId in widgetIds) {
            WidgetUpdateService.updateWidgets(context, this, null, widgetId)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        println("updateWidgets onUpdate")
        // trigger you WidgetUpdater by this call
        for (widgetId in appWidgetIds) {
            WidgetUpdateService.updateWidgets(context, this, null, widgetId)
        }
    }

    override fun onEnabled(context: Context) {
        println("updateWidgets onEnabled")
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, SmallWidgetProvider::class.java)
        )
        // update widgets
        for (widgetId in widgetIds) {
            WidgetUpdateService.updateWidgets(context, this, null, widgetId)
        }
    }

    override fun onDisabled(context: Context) {
        println("updateWidgets onDisabled")
    }
}