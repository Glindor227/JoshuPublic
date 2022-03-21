package com.example.joshu.ui.dialog.dateTimeChoicer

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.joshu.mvp.model.entity.DateRepeatEnum
import com.example.joshu.mvp.view.IBaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface IDateTimeChoicerScreenView: IBaseView {
    fun hideTimeVisible()
    fun showTimeVisible()
    fun hideCalendarVisible()
    fun showCalendarVisible()
    fun hideRepeatVisible()
    fun showRepeatVisible()
    fun showDate(date: String)
    fun showTime(time: String)
    fun showRepeat(repeatType: DateRepeatEnum)
    fun setSelectedDate(year: Int, month: Int, day: Int, hours: Int, minute: Int)

}