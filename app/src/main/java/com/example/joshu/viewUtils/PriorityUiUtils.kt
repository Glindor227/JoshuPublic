package com.example.joshu.viewUtils

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.joshu.R
import com.example.joshu.mvp.model.entity.PriorityTypeEnum

object PriorityUiUtils {
    @DrawableRes
    fun getResourceImage(priorityTypeEnum: PriorityTypeEnum): Int = when (priorityTypeEnum) {
        PriorityTypeEnum.ImportantUrgently -> R.drawable.priority_0
        PriorityTypeEnum.ImportantDoNotRush -> R.drawable.priority_1
        PriorityTypeEnum.NeverMindUrgently -> R.drawable.priority_2
        PriorityTypeEnum.NeverMindDoNotRush -> R.drawable.priority_3
    }

    @StringRes
    fun getName(priorityTypeEnum: PriorityTypeEnum): Int = when (priorityTypeEnum) {
        PriorityTypeEnum.ImportantUrgently -> R.string.priority_important_urgently
        PriorityTypeEnum.ImportantDoNotRush -> R.string.priority_important_do_not_rush
        PriorityTypeEnum.NeverMindUrgently -> R.string.priority_never_mind_urgently
        PriorityTypeEnum.NeverMindDoNotRush -> R.string.priority_never_mind_do_not_rush
    }

    @ColorRes
    fun getColor(priorityTypeEnum: PriorityTypeEnum): Int = when (priorityTypeEnum) {
        PriorityTypeEnum.ImportantUrgently -> R.color.color_dot_priority_0
        PriorityTypeEnum.ImportantDoNotRush -> R.color.color_dot_priority_1
        PriorityTypeEnum.NeverMindUrgently -> R.color.color_dot_priority_2
        PriorityTypeEnum.NeverMindDoNotRush -> R.color.color_dot_priority_3
    }
}