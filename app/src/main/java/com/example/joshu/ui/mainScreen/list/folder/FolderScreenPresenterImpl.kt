package com.example.joshu.ui.mainScreen.list.folder

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.IBufferTaskUtil
import com.example.joshu.mvp.model.IDateFormatter
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.entity.DeleteStatusEnum
import com.example.joshu.mvp.model.entity.room.Task
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.mvp.view.list.ITaskListRowView
import com.example.joshu.ui.mainScreen.baseTaskScreen.BaseTaskScreenPresenterImpl
import com.example.joshu.ui.mainScreen.today.TaskData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@InjectViewState
class FolderScreenPresenterImpl(strings: IStrings, private val folderId: Int) :
    BaseTaskScreenPresenterImpl<IFolderScreenView>(strings) {

    @Inject
    lateinit var bufferTaskUtils: IBufferTaskUtil

    init {
        listPresenter = FolderTaskListPresenterImpl(onClick, onClickExpand, dateTimeFormatter)
    }

    inner class FolderTaskListPresenterImpl(
        onClick: (Int, Task) -> Unit,
        onClickExpand: (Int, Task) -> Unit,
        dateTimeFormatter: IDateFormatter
    ) : TaskListPresenterImpl({ null }, onClick, onClickExpand, dateTimeFormatter) {

        override fun bind(view: ITaskListRowView) {
            super.bind(view)
            view.setFolderVisibility(false)
        }

    }

    override fun initActionBar() {
        viewState.initActionBar()
    }

    override fun getTaskList() {
        getFolderInfoFromDb()
        getTasksFromDB()
    }

    private fun getTasksFromDB() {
        val job = scope.launch {
            val tasks = dbTaskDao.getAllByFolder(folderId, DeleteStatusEnum.No.value)
            val taskList = tasks.sortedWith(
                Comparator { task1, task2 ->
                    val dateTime1 = task1.dateTime
                    val dateTime2 = task2.dateTime
                    val priority1 = task1.priority
                    val priority2 = task2.priority

                    if (dateTime1 == 0L && dateTime2 == 0L) {
                        task1.id.compareTo(task2.id)
                    } else if (dateTime1 > 0L && dateTime2 == 0L) {
                        -1
                    } else if (dateTime1 == 0L && dateTime2 > 0L) {
                        1
                    } else {
                        if (dateTimeFormatter.getDateTimeShot(dateTime1) ==
                            dateTimeFormatter.getDateTimeShot(dateTime2)
                        ) {
                            priority1.compareTo(priority2)
                        } else {
                            dateTime1.compareTo(dateTime2)
                        }
                    }
                }
            )

            listPresenter.clear()
            listPresenter.list.addAll(taskList)

            withContext(Dispatchers.Main) {
                viewState.updateList()
                viewState.enableEmptyView(listPresenter.list.isEmpty())
            }
        }
        addJob(job, "getFolderTasksFromDB")
    }

    private fun getFolderInfoFromDb() {
        val job = scope.launch {
            val folder = dbFolderDao.getById(folderId)
            val folderName = folder.title
            val countOfTasks = dbTaskDao.getCountByFolder(folderId, DeleteStatusEnum.No.value)
            setFolderInNewTaskDialog(folder)

            withContext(Dispatchers.Main) {
                viewState.updateFolderTitle(folderName)
                viewState.updateCountOfTasks(countOfTasks)
            }
        }
        addJob(job, "getFolderInfoFromDB")
    }

    private fun setFolderInNewTaskDialog(folder: TaskFolder?) {
        val taskInfo = bufferTaskUtils.getTaskFromBuffer() ?: TaskData()
        taskInfo.folder = folder
        bufferTaskUtils.putTaskToBuffer(taskInfo)
    }

    override fun onPause() {
        setFolderInNewTaskDialog(null)
        super.onPause()
    }

    override fun updateTask(task: Task) {
        val job = scope.launch {
            dbTaskDao.update(task)
            getTaskList()

            withContext(Dispatchers.Main) {
                viewState.updateWidgetInfo()
            }
        }
        addJob(job, "updateDataToDB")
    }

}