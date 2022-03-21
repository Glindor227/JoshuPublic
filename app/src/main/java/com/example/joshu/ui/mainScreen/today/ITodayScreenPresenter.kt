package com.example.joshu.ui.mainScreen.today

import com.example.joshu.mvp.model.entity.TaskStatusEnum
import com.example.joshu.ui.mainScreen.baseTaskScreen.IBaseTaskScreenPresenter

interface ITodayScreenPresenter : IBaseTaskScreenPresenter {
    fun selectedStatusTask(isChecked: Boolean, taskStatusEnum: TaskStatusEnum)
}