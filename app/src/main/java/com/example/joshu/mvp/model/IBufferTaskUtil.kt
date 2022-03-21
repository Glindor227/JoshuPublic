package com.example.joshu.mvp.model

import com.example.joshu.ui.mainScreen.today.TaskData

interface IBufferTaskUtil {
    fun getTaskFromBuffer(): TaskData?
    fun putTaskToBuffer(taskData: TaskData)
    fun clearTaskBuffer()
}