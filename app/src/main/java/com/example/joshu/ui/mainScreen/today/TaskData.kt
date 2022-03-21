package com.example.joshu.ui.mainScreen.today

import com.example.joshu.mvp.model.entity.DateRepeatEnum
import com.example.joshu.mvp.model.entity.PriorityTypeEnum
import com.example.joshu.mvp.model.entity.room.TaskFolder

data class TaskData (
    var text: String? = null,
    var priority: PriorityTypeEnum = PriorityTypeEnum.DEFAULT,
    var date: Long = System.currentTimeMillis(),
    var folder: TaskFolder? = null,
    var repeatDataTime: DateRepeatEnum = DateRepeatEnum.DEFAULT
)