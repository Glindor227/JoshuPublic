package com.example.joshu.ui.dialog.createTaskBottomSheetDialog

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.IBufferTaskUtil
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.api.network.Result
import com.example.joshu.mvp.model.entity.DateRepeatEnum
import com.example.joshu.mvp.model.entity.PriorityTypeEnum
import com.example.joshu.mvp.model.entity.TaskFactory
import com.example.joshu.mvp.model.entity.TaskStatusEnum
import com.example.joshu.mvp.model.entity.room.Task
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.mvp.model.entity.room.dao.TaskDao
import com.example.joshu.mvp.model.entity.room.dao.TaskFolderDao
import com.example.joshu.mvp.model.observable.IObservableTask
import com.example.joshu.mvp.model.repo.ITaskRepo
import com.example.joshu.ui.dialog.bottomSheetsDialog.BaseBottomSheetDialogPresenterAbs
import com.example.joshu.ui.dialog.dateTimeChoicer.ISelectedDateScreenInteraction
import com.example.joshu.ui.dialog.folderSelected.ISelectedFolderInteraction
import com.example.joshu.ui.formatter.DateFormatterImpl
import com.example.joshu.ui.mainScreen.today.TaskData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@InjectViewState
open class CreateTaskBottomSheetDialogPresenterImpl(
    private val text: String?,
    private val autoCreate: Boolean,
    private val strings: IStrings
) :
    BaseBottomSheetDialogPresenterAbs<ICreateTaskBottomSheetDialogView>(strings),
    ICreateTaskBottomSheetDialogPresenter,
    ISelectedDateScreenInteraction,
    ISelectedFolderInteraction {

    private val dateTimeFormatter = DateFormatterImpl()

    @Inject
    lateinit var taskDao: TaskDao
    @Inject
    lateinit var taskFolderDao: TaskFolderDao
    @Inject
    lateinit var bufferTaskUtils: IBufferTaskUtil
    @Inject
    lateinit var observableTask: IObservableTask
    @Inject
    lateinit var taskRepo: ITaskRepo

    protected var taskData: TaskData? = null

    protected open fun initTaskData(): TaskData =
        bufferTaskUtils.getTaskFromBuffer() ?: TaskData()


    override fun onInitedCreateDialog() {
        viewState.initView()
        val mTaskData = initTaskData()
        fillTaskData(mTaskData)
        taskData = mTaskData

        if (mTaskData.folder == null) {
            getDefaultFolder {
                mTaskData.folder = it
                fillViewOrAutoCreate()
            }
        } else {
            fillViewOrAutoCreate()
        }
    }

    private fun fillTaskData(taskData: TaskData) {
        text?.let {taskData.text = it }
    }

    private fun fillViewOrAutoCreate() {
        if (autoCreate) {
            onSendClick(text)
        } else {
            fillViews()
        }
    }

    protected fun fillViews() {
        val mTaskData = taskData ?: return

        viewState.showResultDateTime(dateTimeFormatter.getDateTimeShot(mTaskData.date))

        mTaskData.text?.let {
            viewState.setText(it)
        }

        viewState.setPriority(mTaskData.priority)

        mTaskData.folder?.let {
            viewState.setFolder(it.title)
        }
    }

    private fun getDefaultFolder(callback: (TaskFolder?) -> Unit) {
        val job = scope.launch {
            val folder = taskFolderDao.getDefault()

            withContext(Dispatchers.Main) {
                callback.invoke(folder)
            }
        }
        addJob(job, "getDefaultFolder")
    }

    override fun onCloseClick() {
        closeScreen()
        bufferTaskUtils.clearTaskBuffer()
    }

    override fun onFolderClick() {
        viewState.showSelectFolderScreen()
    }

    override fun onPriorityClick() {
        viewState.showSelectPriorityScreen()
    }

    override fun onClockClick() {
        val mTaskData = taskData ?: return
        viewState.showSelectDateTimeScreen(mTaskData.date, mTaskData.repeatDataTime)
    }

    override fun onSendClick(text: String?) {
        val newTask = createTask(text) ?: return
        lockScreen()
        val job = scope.launch {
            val exception = try {
                val taskID = taskDao.insert(newTask)
                val result = taskRepo.sendTask(newTask.also { it.id = taskID.toInt() })
                null
            } catch (exception: Throwable) {
                exception
            }


            withContext(Dispatchers.Main) {
                unlockScreen()
                exception?.let {
                    showErrorByThrowable(it)
                } ?: showSuccessAndClose(newTask)
            }
        }

        addJob(job, "create task")
    }

    protected fun createTask(text: String?): Task? {
        val mText = text?.let {
            if (isValidText(text)) {
                it
            } else {
                showError(strings.invalidateTaskText)
                return null
            }
        } ?: run {
            showError(strings.invalidateTaskText)
            return null
        }

        val mTaskData = taskData ?: return null

        val mTaskFolder = mTaskData.folder ?: run {
            showError(strings.invalidateTaskFolder)
            return null
        }

        if (!isValidDateTime(mTaskData.date)) {
            showError(strings.invalidateDateTime)
            return null
        }
        return TaskFactory.create(
            mText,
            mTaskData.priority,
            mTaskData.date,
            mTaskData.repeatDataTime,
            mTaskFolder,
            TaskStatusEnum.Tasks
        )
    }

    override fun setPriorityType(priorityType: PriorityTypeEnum) {
        taskData?.priority = priorityType
        viewState.setPriority(priorityType)
    }

    override fun setDateTimeTask(dateTime: Long, repeatType: DateRepeatEnum) {
        taskData?.date = dateTime
        taskData?.repeatDataTime = repeatType
        viewState.showResultDateTime(dateTimeFormatter.getDateTimeShot(dateTime))
    }

    override fun onDismiss() {
        taskData?.let {
            bufferTaskUtils.putTaskToBuffer(it)
        }
        viewState.sendDismissToInteraction()
    }

    override fun onInputTextChanged(charSequence: CharSequence?) {
        taskData?.text = charSequence?.toString()
    }

    override fun onOutsideClick() {
        closeScreen()
    }

    private fun showSuccessAndClose(newTask: Task) {
        bufferTaskUtils.clearTaskBuffer()
        taskData = null
        observableTask.pushCreatedOrUdatedTask(newTask)
        viewState.updateWidgetInfo()
        showMessage(strings.taskCreateSuccess)
        closeScreen()
    }

    private fun isValidDateTime(date: Long): Boolean = date >= 0L

    private fun isValidText(text: String): Boolean = text.isNotEmpty()

    override fun selectedFolder(folder: TaskFolder) {
        taskData?.folder = folder
        viewState.setFolder(folder.title)
    }
}