package com.example.joshu.ui.dialog.editTaskBottomSheetDialog

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.entity.DateRepeatEnum
import com.example.joshu.mvp.model.entity.PriorityTypeEnum
import com.example.joshu.mvp.model.entity.room.Task
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.ui.dialog.createTaskBottomSheetDialog.CreateTaskBottomSheetDialogPresenterImpl
import com.example.joshu.ui.mainScreen.today.TaskData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@InjectViewState
class EditTaskBottomSheetDialogPresenterImpl(
    private val strings: IStrings,
    private val taskId: Int
) : CreateTaskBottomSheetDialogPresenterImpl(null, false, strings) {

    private var isTaskChanged = false

    override fun onInitedCreateDialog() {
        viewState.initView()
        getTaskFromDB()
    }

    override fun onSendClick(text: String?) {
        if (isTaskChanged) {
            val task = createTask(text) ?: return
            task.id = taskId
            updateTask(task)
        } else {
            closeScreen()
        }
    }

    private fun updateTask(task: Task) {
        lockScreen()
        val job = scope.launch {
            val exception = try {
                task.editDate = System.currentTimeMillis()
                taskDao.update(task)
                taskRepo.updateTask(task)
                isTaskChanged = false
                null
            } catch (exception: Throwable) {
                exception
            }

            withContext(Dispatchers.Main) {
                unlockScreen()
                exception?.let {
                    showErrorByThrowable(it)
                } ?: showSuccessAndClose(task)
            }
        }
        addJob(job, "updateTask")
    }

    private fun getTaskFromDB() {
        val job = scope.launch {
            val taskFromDB = taskDao.getById(taskId)
            val taskFolder = taskFolderDao.getById(taskFromDB.folderId)

            taskData = TaskData(
                taskFromDB.text,
                PriorityTypeEnum.getByValue(taskFromDB.priority),
                taskFromDB.dateTime,
                taskFolder
            )

            withContext(Dispatchers.Main) {
                fillViews()
            }
        }
        addJob(job, "getTaskFromDB")
    }

    override fun onDismiss() {
        viewState.sendDismissToInteraction()
    }

    override fun onCloseClick() {
        closeScreen()
    }

    private fun showSuccessAndClose(task: Task) {
        showMessage(strings.taskUpdateSuccess)
        observableTask.pushCreatedOrUdatedTask(task)
        closeScreen()
    }

    override fun setDateTimeTask(dateTime: Long, repeatType: DateRepeatEnum) {
        setTaskChanged(taskData?.date != dateTime || taskData?.repeatDataTime != repeatType)
        super.setDateTimeTask(dateTime, repeatType)
    }

    override fun setPriorityType(priorityType: PriorityTypeEnum) {
        setTaskChanged(taskData?.priority != priorityType)
        super.setPriorityType(priorityType)
    }

    override fun selectedFolder(folder: TaskFolder) {
        setTaskChanged(taskData?.folder != folder)
        super.selectedFolder(folder)
    }

    override fun onInputTextChanged(charSequence: CharSequence?) {
        setTaskChanged(taskData?.text != charSequence?.toString())
        super.onInputTextChanged(charSequence)
    }

    private fun setTaskChanged(isChanged: Boolean) {
        isTaskChanged = isTaskChanged || isChanged
    }

    override fun onOutsideClick() {
        if (isTaskChanged) {
            viewState.showSaveChangesDialog()
        } else {
            closeScreen()
        }
    }
}
