package com.example.joshu.ui.mainScreen

import com.example.joshu.mvp.presenter.IBasePresenter

interface IMainScreenPresenter: IBasePresenter {
    fun onLongClickAddTask()
    fun setResultSpeechText(texts: ArrayList<String>?)
    fun setFailedResultSpeechText()
    fun setCanceledResultSpeechText()
    fun onClickAddNewTask()
    fun onDismissCreateDialog()
}