package com.example.joshu.viewUtils

import com.example.joshu.R
import com.example.joshu.mvp.model.entity.DateRepeatEnum

object DateRepeatUiUtils {
    fun getTitleStringId(value: DateRepeatEnum): Int {
        return when (value) {
            DateRepeatEnum.RepeatNo -> R.string.repeat_no
            DateRepeatEnum.RepeatEveryDay -> R.string.repeat_every_day
            DateRepeatEnum.RepeatEveryWeek -> R.string.repeat_every_week
            DateRepeatEnum.RepeatEveryMonth -> R.string.repeat_every_month
            DateRepeatEnum.RepeatEveryYear -> R.string.repeat_every_year
            DateRepeatEnum.DEFAULT -> R.string.repeat_no
        }
    }
}