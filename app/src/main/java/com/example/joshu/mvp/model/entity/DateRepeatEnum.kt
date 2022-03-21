package com.example.joshu.mvp.model.entity

enum class DateRepeatEnum(val value: Int) {
    RepeatNo(0),
    RepeatEveryDay(1),
    RepeatEveryWeek(2),
    RepeatEveryMonth(3),
    RepeatEveryYear(4);

    companion object {
        val DEFAULT = RepeatNo
        fun getByValue(value: Int): DateRepeatEnum =
            values().find { it.value == value } ?: DEFAULT
    }
}