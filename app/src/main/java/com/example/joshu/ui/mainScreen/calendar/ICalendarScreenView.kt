package com.example.joshu.ui.mainScreen.calendar

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.joshu.mvp.model.CalendarDate
import com.example.joshu.ui.mainScreen.baseTaskScreen.IBaseTaskScreenView
import com.example.joshu.ui.mainScreen.calendar.CalendarScreenPresenterImpl.DataDecoration

@StateStrategyType(OneExecutionStateStrategy::class)
interface ICalendarScreenView: IBaseTaskScreenView {
    fun createDecoration(dataDecoration: DataDecoration)
    fun initActionBar(text: String)
    fun setSelectedDate(date: CalendarDate)
    fun setMinMaxDateToCalendar(minDate: CalendarDate, maxDate: CalendarDate)
    fun onAppBarCollapsed()
    fun onAppBarExpanded()
}