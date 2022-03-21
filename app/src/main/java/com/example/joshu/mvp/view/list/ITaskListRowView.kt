package com.example.joshu.mvp.view.list

import com.example.joshu.mvp.model.entity.PriorityTypeEnum
import com.example.joshu.mvp.model.entity.room.TaskFolder

interface ITaskListRowView: IBaseListRowView {
    fun setPriority(priorityTypeEnum: PriorityTypeEnum)
    fun setTitle(title: String)
    fun setTaskFolder(taskFolder: TaskFolder)
    fun setDateTime(dateTime: String)
    fun setFolderVisibility(isVisible: Boolean)
    fun setStatus(status: Int)
}