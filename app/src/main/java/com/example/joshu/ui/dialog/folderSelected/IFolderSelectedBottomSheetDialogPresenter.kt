package com.example.joshu.ui.dialog.folderSelected

import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.mvp.presenter.IBasePresenter

interface IFolderSelectedBottomSheetDialogPresenter: IBasePresenter {
    fun onClickCreateFolder()
    fun onSelectFolder(folder: TaskFolder)
    fun onConfirmCreateFolderDialog(folderId: Int)
}