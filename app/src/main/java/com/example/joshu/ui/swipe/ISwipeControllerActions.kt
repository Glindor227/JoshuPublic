package com.example.joshu.ui.swipe

import com.example.joshu.mvp.model.entity.TaskStatusEnum

interface ISwipeControllerActions {
    fun onDeleteSwipeButtonClick(position: Int)
    fun onSendSwipeButtonClick(position: Int)
    fun onCalendarSwipeButtonClick(position: Int)
    fun onDoingSwipeButtonClick(position: Int, status: TaskStatusEnum)
    fun onDeleteCancelClicked()
    fun onDeleteFinished()
    fun onSendOkClicked()
}