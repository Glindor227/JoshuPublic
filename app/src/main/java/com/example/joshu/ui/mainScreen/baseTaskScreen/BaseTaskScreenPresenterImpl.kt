package com.example.joshu.ui.mainScreen.baseTaskScreen

import com.example.joshu.mvp.model.IDateFormatter
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.api.network.Result
import com.example.joshu.mvp.model.entity.DateRepeatEnum
import com.example.joshu.mvp.model.entity.DeleteStatusEnum
import com.example.joshu.mvp.model.entity.PriorityTypeEnum
import com.example.joshu.mvp.model.entity.TaskStatusEnum
import com.example.joshu.mvp.model.entity.room.Task
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.mvp.model.entity.room.dao.TaskDao
import com.example.joshu.mvp.model.entity.room.dao.TaskFolderDao
import com.example.joshu.mvp.model.repo.ITaskRepo
import com.example.joshu.mvp.presenter.BasePresenterAbs
import com.example.joshu.mvp.presenter.list.BaseListPresenter
import com.example.joshu.mvp.presenter.list.ITaskListPresenter
import com.example.joshu.mvp.view.list.ITaskListRowView
import com.example.joshu.ui.formatter.DateFormatterImpl
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseTaskScreenPresenterImpl<T : IBaseTaskScreenView>(strings: IStrings) :
    BasePresenterAbs<T>(strings), IBaseTaskScreenPresenter {

    @Inject
    lateinit var dbTaskDao: TaskDao

    @Inject
    lateinit var dbFolderDao: TaskFolderDao

    @Inject
    lateinit var taskRepo: ITaskRepo

    lateinit var listPresenter: TaskListPresenterImpl

    val dateTimeFormatter = DateFormatterImpl()

    protected var operationTaskPosition: Int = 0
    private var deleteTaskId: Int = 0

    val onClick: (Int, Task) -> Unit = { position, task ->
        viewState.onEditTaskDialog(task.id)
    }
    val onClickExpand: (Int, Task) -> Unit = { position, task ->
        // TODO
    }

    open class TaskListPresenterImpl(
        private val onGetTaskFolderById: (Int) -> TaskFolder?,
        private val onClick: (Int, Task) -> Unit,
        private val onClickExpand: (Int, Task) -> Unit,
        private val dateTimeFormatter: IDateFormatter
    ) : BaseListPresenter<Task>(), ITaskListPresenter {

        override fun onClickExpand(position: Int) {
            val item = getItemByPosition(position)
            onClickExpand.invoke(position, item)
        }

        override fun bind(view: ITaskListRowView) {
            val item = getItemByPosition(view.getPos())

            view.setTitle(item.text)
            view.setStatus(item.status)
            view.setPriority(PriorityTypeEnum.getByValue(item.priority))

            if (item.dateTime > 0) {
                view.setDateTime(dateTimeFormatter.getDateTimeShot(item.dateTime))
            }

            onGetTaskFolderById.invoke(item.folderId)?.let {
                view.setTaskFolder(it)
            }
        }

        override fun onClick(position: Int) {
            val item = getItemByPosition(position)
            onClick.invoke(position, item)
        }
    }

    abstract fun getTaskList()

    abstract fun initActionBar()

    abstract fun updateTask(task: Task)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initView()
        viewState.initRecyclerView()
        viewState.enableEmptyView(false)
        initActionBar()
    }

    override fun onResume() {
        super.onResume()
        getTaskList()
    }

    override fun getListPresenter(): ITaskListPresenter = listPresenter

    override fun onDismissCreateDialog() {
        getTaskList()
        viewState.updateWidgetInfo()
    }

    /**
     * ISwipeControllerActions
     */
    override fun onDeleteSwipeButtonClick(position: Int) {
        val task = listPresenter.getItemByPosition(position)
        // для операции удаления не годится запоминать позицию задачи в списке
        // так как она удаляется из списка до операции восстоновления или подтверждения удаления
        deleteTaskId = task.id
        task.deleteStatus = DeleteStatusEnum.Deleting.value
        updateTask(task)
        viewState.deleteTask(task.text)
    }

    override fun onSendSwipeButtonClick(position: Int) {
        saveTaskPosition(position)
        viewState.sendTask()
    }

    override fun onCalendarSwipeButtonClick(position: Int) {
        val task = saveTaskPosition(position)
        viewState.postponeTask(task.dateTime, task.repeatType)
    }

    override fun onDoingSwipeButtonClick(position: Int, status: TaskStatusEnum) {
        val task = saveTaskPosition(position)
        if (status == TaskStatusEnum.Doing) {
            task.status = TaskStatusEnum.Finished.value
        } else {
            task.status = TaskStatusEnum.Doing.value
        }

        updateTask(task)
    }

    override fun onDeleteCancelClicked() = updateDeleteStatus(DeleteStatusEnum.No.value)

    override fun onDeleteFinished() = updateDeleteStatus(DeleteStatusEnum.Deleted.value)

    private fun updateDeleteStatus(deleteStatus: Int) {
        val job = scope.launch {
            val taskDeleting = dbTaskDao.getById(deleteTaskId)
            taskDeleting.deleteStatus = deleteStatus
            updateTask(taskDeleting)
            val serverDeletingResult = taskRepo.deleteTask(deleteTaskId)

            when (serverDeletingResult) {
                is Result.Success -> dbTaskDao.deleteById(deleteTaskId)
            }
        }
        addJob(job, "updateDeleteStatus")
    }

    override fun onSendOkClicked() {
        val task = listPresenter.getItemByPosition(operationTaskPosition)
        task.status = TaskStatusEnum.Wait.value
        // тут надо отсылать it Это отдельная задача
        updateTask(task)
    }

    private fun saveTaskPosition(position: Int): Task {
        operationTaskPosition = position
        return listPresenter.getItemByPosition(position)
    }

    override fun checkedReverse(isChecked: Boolean) {
        //TODO("Not yet implemented")
    }

    override fun setDateTimeTask(dateTime: Long, repeatType: DateRepeatEnum) {
        val task = listPresenter.getItemByPosition(operationTaskPosition)
        task.dateTime = dateTime
        task.repeatType = repeatType.value
        updateTask(task)
    }
}