package com.example.joshu.ui.dialog.createFolderBottomSheetDialog

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.entity.TaskFolderFactory
import com.example.joshu.mvp.model.entity.room.dao.TaskFolderDao
import com.example.joshu.ui.dialog.bottomSheetsDialog.BaseBottomSheetDialogPresenterAbs
import com.example.joshu.viewUtils.ColorUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@InjectViewState
class CreateFolderBottomSheetDialogPresenterImpl(private val strings: IStrings) :
    BaseBottomSheetDialogPresenterAbs<ICreateFolderBottomSheetDialogView>(strings),
    ICreateFolderBottomSheetDialogPresenter {

    @Inject
    lateinit var taskFolderDao: TaskFolderDao

    override fun onSendClick(text: String?) {
        if (text == null || !isValidText(text)) {
            showError(strings.invalidateTaskText)
            return
        }
        lockScreen()
        val job = scope.launch {
            var folderId = 0L
            val e = try {
                folderId = taskFolderDao.insert(
                    TaskFolderFactory.create(
                        text,
                        ColorUtils.getRandomBrightColor()
                    )
                )
                null
            } catch (e: Throwable) {
                e
            }

            withContext(Dispatchers.Main) {
                unlockScreen()
                e?.let {
                    showErrorByThrowable(it)
                } ?: showSuccessAndClose(folderId.toInt())
            }
        }
        addJob(job, "create folder")
    }

    override fun onInitedCreateDialog() {
        viewState.initView()
    }

    private fun showSuccessAndClose(folderId: Int) {
        showMessage(strings.folderCreateSuccess)
        viewState.sendConfirmToInteraction(folderId)
        closeScreen()
    }

    override fun onDismiss() {
    }


    private fun isValidText(text: String): Boolean = text.isNotEmpty()
}