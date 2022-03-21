package com.example.joshu.mvp.model.entity

import com.example.joshu.mvp.model.entity.room.Task
import com.example.joshu.mvp.model.entity.room.TaskFolder

object TaskFactory {
    fun create(text: String, priority: PriorityTypeEnum, dateTime: Long, repeatType:DateRepeatEnum, taskFolder: TaskFolder,
               taskStatus: TaskStatusEnum): Task =
        Task(text = text, priority = priority.value, dateTime = dateTime, repeatType = repeatType.value, folderId = taskFolder.id,
            status = taskStatus.value, deleteStatus = DeleteStatusEnum.No.value)
}