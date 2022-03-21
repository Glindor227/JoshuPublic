package com.example.joshu.mvp.model.observable

import com.example.joshu.mvp.model.entity.room.Task

interface ISubscriberTask {
    fun onCreatedOrUpdatedTask(task: Task)
}