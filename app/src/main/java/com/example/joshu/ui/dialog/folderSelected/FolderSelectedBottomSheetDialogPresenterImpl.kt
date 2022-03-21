package com.example.joshu.ui.dialog.folderSelected

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.mvp.model.entity.room.dao.TaskFolderDao
import com.example.joshu.ui.dialog.bottomSheetsDialog.BaseBottomSheetDialogPresenterAbs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@InjectViewState
class FolderSelectedBottomSheetDialogPresenterImpl(strings: IStrings):
    BaseBottomSheetDialogPresenterAbs<IFolderSelectedBottomSheetDialogView>(strings),
    IFolderSelectedBottomSheetDialogPresenter {

    @Inject
    lateinit var dbTaskFolderDao: TaskFolderDao

    override fun onInitedCreateDialog() {
        viewState.initView()

        loadFolderFromDB()
    }

    private fun loadFolderFromDB() {
        val job = scope.launch {
            val folders = dbTaskFolderDao.getAll()

            withContext(Dispatchers.Main) {
                if (folders.isNotEmpty()) {
                    viewState.showFolders(folders)
                }
            }
        }
        addJob(job, "loadFolderFromDB")
    }

    private fun selectFolderFromDB(folderId: Int) {
        val job = scope.launch {
            val folder = dbTaskFolderDao.getById(folderId)

            withContext(Dispatchers.Main) {
                onSelectFolder(folder)
            }
        }
        addJob(job, "selectFolderFromDB")
    }

    override fun onClickCreateFolder() {
        viewState.showCreateFolderScreen()
    }

    override fun onSelectFolder(folder: TaskFolder) {
        viewState.setSelectedFolder(folder)
        closeScreen()
    }

    override fun onConfirmCreateFolderDialog(folderId: Int) {
        selectFolderFromDB(folderId)
    }
}