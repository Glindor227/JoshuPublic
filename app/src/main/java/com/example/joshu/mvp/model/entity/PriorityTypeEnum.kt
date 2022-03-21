package com.example.joshu.mvp.model.entity

enum class PriorityTypeEnum(val value: Int) {
    ImportantUrgently(0),
    ImportantDoNotRush(1),
    NeverMindUrgently(2),
    NeverMindDoNotRush(3);

    companion object {
        val DEFAULT = NeverMindDoNotRush
        fun getByValue(value: Int): PriorityTypeEnum =
            values().find { it.value == value } ?: DEFAULT
    }
}