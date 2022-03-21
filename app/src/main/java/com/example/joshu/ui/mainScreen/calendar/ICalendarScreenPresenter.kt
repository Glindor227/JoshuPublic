package com.example.joshu.ui.mainScreen.calendar

import com.example.joshu.mvp.model.CalendarDate
import com.example.joshu.mvp.model.observable.ISubscriberTask
import com.example.joshu.mvp.presenter.IBasePresenter

interface ICalendarScreenPresenter: IBasePresenter, ISubscriberTask {
    fun onDateSelected(date: CalendarDate)
    fun onAppBarCollapsed()
    fun onAppBarExpanded()
}