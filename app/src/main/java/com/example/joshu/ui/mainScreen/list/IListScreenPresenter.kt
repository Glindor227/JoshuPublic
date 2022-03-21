package com.example.joshu.ui.mainScreen.list

import com.example.joshu.mvp.presenter.IBasePresenter
import com.example.joshu.mvp.presenter.list.IFolderListPresenter

interface IListScreenPresenter : IBasePresenter {
    fun getListPresenter(): IFolderListPresenter
    fun onDismissCreateDialog()
    fun onConfirmCreateFolderDialog(folderId: Int)
    fun onConfirmDeleteDialog()
}