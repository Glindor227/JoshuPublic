package com.example.joshu.viewUtils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.joshu.R
import com.example.joshu.mvp.model.entity.TaskStatusEnum

object StatusUiUtils {

    fun getVisible(statusEnum: TaskStatusEnum): Boolean = when(statusEnum) {
                TaskStatusEnum.Finished -> false
        else -> true
    }
    @StringRes
    fun getName(statusEnum: TaskStatusEnum): Int = when(statusEnum) {
        TaskStatusEnum.Tasks -> R.string.chip_tasks
        TaskStatusEnum.Doing -> R.string.chip_doing
        TaskStatusEnum.Wait -> R.string.chip_wait
        TaskStatusEnum.Habits -> R.string.chip_habits
        TaskStatusEnum.Finished -> R.string.chip_finish
    }

    @DrawableRes
    fun getIcon(statusEnum: TaskStatusEnum): Int = when(statusEnum) {
        TaskStatusEnum.Tasks -> R.drawable.ic_tasksinactive
        TaskStatusEnum.Doing -> R.drawable.ic_doinginactive
        TaskStatusEnum.Habits -> R.drawable.ic_habbits_chip_checked
        TaskStatusEnum.Wait -> R.drawable.ic_waitinginactive
        TaskStatusEnum.Finished -> R.drawable.ic_doneinactive
    }

    @DrawableRes
    fun getActivityIcon(index: Int, isChecked: Boolean): Int = when(index) {
        TaskStatusEnum.Tasks.value -> {
            if (isChecked) R.drawable.ic_tasksactive
            else R.drawable.ic_tasksinactive
        }
        TaskStatusEnum.Doing.value -> {
            if (isChecked) R.drawable.ic_doingactive
            else R.drawable.ic_doinginactive
        }
        TaskStatusEnum.Habits.value -> {
            if (isChecked) R.drawable.ic_habbitsactive
            else R.drawable.ic_habbits_chip_checked
        }
        TaskStatusEnum.Wait.value -> {
            if (isChecked) R.drawable.ic_waitingactive
            else R.drawable.ic_waitinginactive
        }
        TaskStatusEnum.Finished.value -> {
            if (isChecked) R.drawable.ic_doneactive
            else R.drawable.ic_doneinactive
        }
        else -> R.drawable.ic_close
    }

    fun getTextColor(isChecked: Boolean): Int {
        return if (isChecked){
            R.color.white
        } else {
            R.color.colorPrimary
        }
    }

    fun getBackground(isChecked: Boolean): Int {
        return if (isChecked){
            R.drawable.bg_backdrop_blue
        } else {
            R.drawable.bg_backdrop_white
        }
    }
}