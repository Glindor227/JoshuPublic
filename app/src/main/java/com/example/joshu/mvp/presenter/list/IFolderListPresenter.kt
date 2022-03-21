package com.example.joshu.mvp.presenter.list

import com.example.joshu.mvp.view.list.IFolderListRowView

interface IFolderListPresenter : IBaseListPresenter<IFolderListRowView> {
    fun onLongClick(position: Int)
}