package com.example.joshu.mvp.model.entity

enum class TaskStatusEnum(val value: Int) {
    Tasks(0),
    Wait(1),
    Doing(2),
    Habits(3),
    Finished(4);

    companion object {
        val DEFAULT = Tasks
        fun getByValue(value: Int): TaskStatusEnum =
            values().find { it.value == value } ?: DEFAULT
    }
}