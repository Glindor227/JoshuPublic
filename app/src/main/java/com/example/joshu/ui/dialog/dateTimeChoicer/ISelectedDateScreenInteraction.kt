package com.example.joshu.ui.dialog.dateTimeChoicer

import com.example.joshu.mvp.model.entity.DateRepeatEnum

interface ISelectedDateScreenInteraction {
    fun setDateTimeTask(dateTime: Long, repeatType: DateRepeatEnum)
}