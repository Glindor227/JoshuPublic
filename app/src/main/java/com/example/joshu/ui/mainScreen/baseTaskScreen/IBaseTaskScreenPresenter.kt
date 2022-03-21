package com.example.joshu.ui.mainScreen.baseTaskScreen

import com.example.joshu.mvp.presenter.IBasePresenter
import com.example.joshu.mvp.presenter.list.ITaskListPresenter
import com.example.joshu.ui.dialog.dateTimeChoicer.ISelectedDateScreenInteraction
import com.example.joshu.ui.swipe.ISwipeControllerActions

interface IBaseTaskScreenPresenter : IBasePresenter, ISwipeControllerActions,
    ISelectedDateScreenInteraction {
    fun getListPresenter(): ITaskListPresenter
    fun onDismissCreateDialog()
    fun checkedReverse(isChecked: Boolean)
}