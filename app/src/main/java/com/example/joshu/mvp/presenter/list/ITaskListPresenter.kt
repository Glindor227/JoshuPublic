package com.example.joshu.mvp.presenter.list

import com.example.joshu.mvp.view.list.ITaskListRowView

interface ITaskListPresenter: IBaseListPresenter<ITaskListRowView> {
    fun onClickExpand(position: Int)
}