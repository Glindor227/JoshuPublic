package com.example.joshu.ui.mainScreen.today

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.entity.DeleteStatusEnum
import com.example.joshu.mvp.model.entity.TaskStatusEnum
import com.example.joshu.mvp.model.entity.room.Task
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.ui.mainScreen.baseTaskScreen.BaseTaskScreenPresenterImpl
import com.example.joshu.utils.ComparatorTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@InjectViewState
class TodayScreenPresenterImpl(private val strings: IStrings) :
    BaseTaskScreenPresenterImpl<ITodayScreenView>(strings), ITodayScreenPresenter {

    private val cacheTaskFolder = HashMap<Int, TaskFolder>()
    private val onGetTaskFolderById: (Int) -> TaskFolder? = {
        cacheTaskFolder[it]
    }
    private val menuItemScorePresenter = hashMapOf<Int, Int>()
    private var currentTaskStatus = TaskStatusEnum.DEFAULT

    init {
        listPresenter = TaskListPresenterImpl(
            onGetTaskFolderById, onClick, onClickExpand,
            dateTimeFormatter
        )
    }

    override fun initActionBar() {
        val nowDate = dateTimeFormatter.getDayMonthFull(System.currentTimeMillis())
        viewState.initActionBar(strings.nowPrefix(nowDate))
    }

    override fun getTaskList() {
        getScoreByStatus()
        getTasksFromDB(currentTaskStatus.value)
    }

    override fun updateTask(task: Task) {
        val job = scope.launch {
            dbTaskDao.update(task)
            getTasksFromDB(currentTaskStatus.value)

            withContext(Dispatchers.Main) {
                getScoreByStatus()
                viewState.updateList()
                viewState.updateWidgetInfo()
            }
        }
        addJob(job, "updateDataToDB")
    }

    private fun getTasksFromDB(status: Int? = null) {
        val job = scope.launch {
            val tasks = getTasksByStatus(status)
            val taskList = ComparatorTask.sortTasksByDate(tasks)

            listPresenter.clear()
            listPresenter.list.addAll(taskList)

            updateCacheTaskFolder(taskList.map { it.folderId })

            withContext(Dispatchers.Main) {
                viewState.updateList()
                viewState.enableEmptyView(listPresenter.list.isEmpty())
            }
        }
        addJob(job, "getDataFromDB")
    }

    private suspend fun getTasksByStatus(status: Int?): List<Task> {
        return status?.let {
            dbTaskDao.getAllByStatus(status, DeleteStatusEnum.No.value)
        } ?: run {
            dbTaskDao.getAllByDeleteStatus(DeleteStatusEnum.No.value)
        }
    }

    fun getMenuItemScorePresenter(): HashMap<Int, Int> = menuItemScorePresenter

    private fun getScoreByStatus() {
        menuItemScorePresenter.clear()
        val job = scope.launch {
            TaskStatusEnum.values().forEach {
                menuItemScorePresenter[it.value] =
                    dbTaskDao.getCountByStatus(it.value, DeleteStatusEnum.No.value)
            }

            withContext(Dispatchers.Main) {
                viewState.updateMenuItems()
            }
        }
        addJob(job, "updateMenuItemScore")
    }

    private fun updateCacheTaskFolder(ids: List<Int>) {
        cacheTaskFolder.clear()

        if (ids.isNotEmpty()) {
            val job = scope.launch {
                dbFolderDao.getByFilteredId(ids).forEach {
                    cacheTaskFolder[it.id] = it
                }
            }
            addJob(job, "updateCacheTaskFolder")
        }
    }

    override fun selectedStatusTask(isChecked: Boolean, taskStatusEnum: TaskStatusEnum) {
        if (isChecked) {
            currentTaskStatus = taskStatusEnum
            getTasksFromDB(taskStatusEnum.value)
        } else {
            currentTaskStatus = TaskStatusEnum.DEFAULT
            getTasksFromDB()
        }
    }

}