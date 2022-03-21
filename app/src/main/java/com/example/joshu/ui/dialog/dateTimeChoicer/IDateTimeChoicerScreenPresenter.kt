package com.example.joshu.ui.dialog.dateTimeChoicer

import com.example.joshu.mvp.model.entity.DateRepeatEnum
import com.example.joshu.mvp.presenter.IBasePresenter

interface IDateTimeChoicerScreenPresenter: IBasePresenter {
    fun dateSelected(year: Int, month: Int, day: Int)
    fun repeatSelected(repeatType: DateRepeatEnum)
    fun timeTextClick(visible: Boolean)
    fun dateTextClick(visible: Boolean)
    fun repeatTextClick(visible: Boolean)
    fun timeOkButtonClick(hours: Int, minute: Int)
    fun timeCancelButtonClick()
}