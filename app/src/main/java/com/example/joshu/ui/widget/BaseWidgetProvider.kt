package com.example.joshu.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.joshu.R
import com.example.joshu.ui.splashScreen.SplashScreenActivityImpl

abstract class BaseWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray?
    ) {
        appWidgetIds?.forEach { updateAppWidget(context, it, appWidgetManager) }
    }

    open fun updateAppWidget(
        context: Context,
        appWidgetId: Int,
        appWidgetManager: AppWidgetManager
    ) {
        val views: RemoteViews = RemoteViews(
            context.packageName,
            getLayoutId()
        ).apply { configureRemoteViews(this, context, appWidgetId) }
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    open fun configureRemoteViews(views: RemoteViews, context: Context, appWidgetId: Int) {
        views.setOnClickPendingIntent(R.id.task_text, startAddNewTaskDialog(context))
        views.setOnClickPendingIntent(R.id.btn_add, startAddNewTaskDialog(context))
        views.setOnClickPendingIntent(R.id.btn_voice, startVoiceRecording())
        views.setOnClickPendingIntent(R.id.widget_header, startApp(context))
    }

    protected fun startAddNewTaskDialog(context: Context?): PendingIntent {
        return Intent(context, WidgetAddTaskActivity::class.java)
            .let { intent ->
                PendingIntent.getActivity(context, 0, intent, 0)
            }
    }

    private fun startVoiceRecording(): PendingIntent? {
        // TODO add voice recording intent here
        return null
    }

    fun startApp(context: Context?): PendingIntent? {
        return Intent(context, SplashScreenActivityImpl::class.java)
            .let { intent ->
                PendingIntent.getActivity(context, 0, intent, 0)
            }
    }


    abstract fun getLayoutId(): Int
}