package com.example.joshu.ui.mainScreen.list.folder

import com.example.joshu.ui.mainScreen.baseTaskScreen.IBaseTaskScreenView

interface IFolderScreenView : IBaseTaskScreenView {
    fun initActionBar()
    fun updateFolderTitle(title: String)
    fun updateCountOfTasks(tasksCount: Int)
}