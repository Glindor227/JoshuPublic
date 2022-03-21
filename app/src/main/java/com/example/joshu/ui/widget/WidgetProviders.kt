package com.example.joshu.ui.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.ISharedPreferences
import javax.inject.Inject

class SmallVoiceWidgetProvider : BaseWidgetProvider() {
    override fun getLayoutId(): Int = R.layout.widget_smallvoice
}

class SmallAddWidgetProvider : BaseWidgetProvider() {
    override fun getLayoutId(): Int = R.layout.widget_smalladd
}

class LargeWidgetProvider : BaseWidgetProvider() {
    override fun getLayoutId(): Int = R.layout.widget_large
}

class TaskListWidgetProvider : BaseWidgetProvider() {

    companion object {
        const val ITEM_ID = "ITEM_ID"

        fun notifyWidgetDataChanged(context: Context) {
            val widgetUpdateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
                component = ComponentName(
                    context, TaskListWidgetProvider::class.java
                )
            }
            context.sendBroadcast(widgetUpdateIntent)
        }
    }

    @Inject
    lateinit var sharedPreferences: ISharedPreferences

    override fun getLayoutId(): Int = if (sharedPreferences.isSmallWidget) {
        R.layout.widget_tasklist_small
    } else {
        R.layout.widget_tasklist
    }

    override fun updateAppWidget(
        context: Context,
        appWidgetId: Int,
        appWidgetManager: AppWidgetManager
    ) {
        updateSharedPreferences(context, appWidgetId)
        super.updateAppWidget(context, appWidgetId, appWidgetManager)
    }

    private fun isWidgetHeightSmall(context: Context, appWidgetId: Int): Boolean {
        val height = AppWidgetManager.getInstance(context).getAppWidgetOptions(appWidgetId)
            .getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)
        val twoCellsHeight = context.getDimensionInDP(R.dimen.widget_2_cells)
        return height < twoCellsHeight
    }

    override fun configureRemoteViews(views: RemoteViews, context: Context, appWidgetId: Int) {
        super.configureRemoteViews(views, context, appWidgetId)
        views.setRemoteAdapter(
            R.id.tasklist_view,
            startTaskListViewService(context, appWidgetId)
        )
        views.setEmptyView(R.id.tasklist_view, R.id.empty_tasklist_view)
        views.setOnClickPendingIntent(R.id.widget_date_header, startApp(context))
        views.setOnClickPendingIntent(R.id.empty_tasklist_view, startApp(context))
        views.setPendingIntentTemplate(R.id.tasklist_view, startAddNewTaskDialog(context))
    }

    private fun startTaskListViewService(context: Context?, appWidgetId: Int): Intent {
        return Intent(context, TaskListWidgetService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent?) {
        Injector.getInstance().appComponent().inject(this)
        if (intent?.action in listOf(
                AppWidgetManager.ACTION_APPWIDGET_UPDATE,
                Intent.ACTION_TIMEZONE_CHANGED,
                Intent.ACTION_TIME_CHANGED
            )
        ) {
            val manager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, TaskListWidgetProvider::class.java)
            manager.notifyAppWidgetViewDataChanged(
                manager.getAppWidgetIds(componentName),
                R.id.tasklist_view
            )
        }
        super.onReceive(context, intent)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        updateAppWidget(context, appWidgetId, appWidgetManager)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.tasklist_view)
    }

    private fun Context.getDimensionInDP(resId: Int): Float {
        return resources.getDimension(resId) / resources.displayMetrics.density
    }

    private fun updateSharedPreferences(context: Context, appWidgetId: Int) {
        val isSmallWidget = isWidgetHeightSmall(context, appWidgetId)
        if (sharedPreferences.isSmallWidget != isSmallWidget) {
            sharedPreferences.isSmallWidget = isSmallWidget
        }
    }

}