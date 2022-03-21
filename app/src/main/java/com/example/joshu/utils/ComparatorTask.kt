package com.example.joshu.utils

import com.example.joshu.mvp.model.entity.room.Task

object ComparatorTask {
    fun sortTasksByDate(listTasks: List<Task>): List<Task> =
        listTasks.sortedWith(
                Comparator { task1, task2 ->
                    val dateTime1 = task1.dateTime
                    val dateTime2 = task2.dateTime

                    if (dateTime1 == 0L && dateTime2 == 0L) {
                        task1.id.compareTo(task2.id)
                    } else if (dateTime1 > 0L && dateTime2 == 0L) {
                        -1
                    } else if (dateTime1 == 0L && dateTime2 > 0L) {
                        1
                    } else {
                        dateTime1.compareTo(dateTime2)
                    }
                })
}