package com.example.joshu.observable

import com.example.joshu.mvp.model.entity.room.Task
import com.example.joshu.mvp.model.observable.IObservableTask
import com.example.joshu.mvp.model.observable.ISubscriberTask

class ObservableTaskImpl : IObservableTask {

    private val subscribersList = HashSet<ISubscriberTask>()

    override fun subscribeToNewTask(subscriber: ISubscriberTask) {
        subscribersList.add(subscriber)
    }

    override fun unSubscribeToNewTask(subscriber: ISubscriberTask) {
        subscribersList.remove(subscriber)
    }

    override fun pushCreatedOrUdatedTask(task: Task) {
        subscribersList.forEach {
            it.onCreatedOrUpdatedTask(task)
        }
    }
}