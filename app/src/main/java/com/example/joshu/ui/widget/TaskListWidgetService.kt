package com.example.joshu.ui.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.ISharedPreferences
import com.example.joshu.mvp.model.entity.DeleteStatusEnum
import com.example.joshu.mvp.model.entity.PriorityTypeEnum
import com.example.joshu.mvp.model.entity.room.Task
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.mvp.model.entity.room.dao.TaskDao
import com.example.joshu.mvp.model.entity.room.dao.TaskFolderDao
import com.example.joshu.ui.formatter.DateFormatterImpl
import com.example.joshu.viewUtils.PriorityUiUtils
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class TaskListWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return TaskListRemoteViewsFactory(this.applicationContext)
    }
}

class TaskListRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {


    private lateinit var widgetItems: List<Task>
    private val cacheTaskFolder = HashMap<Int, TaskFolder>()
    private val dateFormatter = DateFormatterImpl()

    private val smallWidgetItemsCount = 1

    @Inject
    lateinit var dbTaskDao: TaskDao

    @Inject
    lateinit var dbFolderDao: TaskFolderDao

    @Inject
    lateinit var sharedPreferences: ISharedPreferences

    override fun onCreate() {
        Injector.getInstance().appComponent().inject(this)
        widgetItems = ArrayList()
    }

    override fun onDataSetChanged() {
        runBlocking {
            launch {
                getDataFromDB()
            }
        }
    }

    override fun onDestroy() {
        widgetItems = emptyList()
    }

    override fun getCount(): Int =
        if (sharedPreferences.isSmallWidget && widgetItems.isNotEmpty()) {
            smallWidgetItemsCount
        } else {
            widgetItems.size
        }

    override fun getViewAt(position: Int): RemoteViews? {
        val item = widgetItems[position]
        val res = RemoteViews(context.packageName, R.layout.widget_tasklist_item)
        res.apply {
            setImageViewResource(
                R.id.priorityView,
                PriorityUiUtils.getResourceImage(PriorityTypeEnum.getByValue(item.priority))
            )
            setTextViewText(R.id.titleView, item.text)
            setTextViewText(R.id.folderView, cacheTaskFolder[item.folderId]?.title)
            setTextViewText(
                R.id.timeView, dateFormatter.getTimeShortFromMillis(item.dateTime)
            )
            setOnClickFillInIntent(R.id.widgetItem, Intent().apply {
                putExtra(TaskListWidgetProvider.ITEM_ID, item.id)
            })
        }
        return res
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return widgetItems[position].id.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    private suspend fun getDataFromDB() {
        val todayStartTime = dateFormatter.getTodayWithTime(0, 0, 0, 0)
        val todayEndTime = dateFormatter.getTodayWithTime(23, 59, 59, 999)
        val tasks = dbTaskDao.getAllByBetweenDateTime(todayStartTime, todayEndTime, DeleteStatusEnum.No.value)
        if (widgetItems != tasks) {
            widgetItems = tasks
            updateCacheTaskFolder(widgetItems.map { it.folderId })
        }
    }

    private suspend fun updateCacheTaskFolder(ids: List<Int>) {
        cacheTaskFolder.clear()
        if (ids.isNotEmpty()) {
            dbFolderDao.getByFilteredId(ids).forEach {
                cacheTaskFolder[it.id] = it
            }
        }
    }

}