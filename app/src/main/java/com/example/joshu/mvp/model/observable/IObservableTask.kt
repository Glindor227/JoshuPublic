package com.example.joshu.mvp.model.observable

import com.example.joshu.mvp.model.entity.room.Task

interface IObservableTask {
    fun subscribeToNewTask(subscriber: ISubscriberTask)
    fun unSubscribeToNewTask(subscriber: ISubscriberTask)
    fun pushCreatedOrUdatedTask(task: Task)
}