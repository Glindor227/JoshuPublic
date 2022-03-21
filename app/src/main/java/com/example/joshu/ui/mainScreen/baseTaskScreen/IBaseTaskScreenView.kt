package com.example.joshu.ui.mainScreen.baseTaskScreen

import com.example.joshu.mvp.view.IBaseView
import com.example.joshu.ui.widget.WidgetInfoInteraction

interface IBaseTaskScreenView : IBaseView, WidgetInfoInteraction {
    fun initRecyclerView()
    fun updateList()
    fun enableEmptyView(enabled: Boolean)
    fun postponeTask(oldDateTime: Long, repeatType: Int)
    fun deleteTask(taskName :String)
    fun sendTask()
    fun onEditTaskDialog(taskId: Int)
}