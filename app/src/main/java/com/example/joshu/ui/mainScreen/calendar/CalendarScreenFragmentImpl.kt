package com.example.joshu.ui.mainScreen.calendar

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.CalendarDate
import com.example.joshu.mvp.model.entity.PriorityTypeEnum
import com.example.joshu.ui.mainScreen.baseTaskScreen.BaseTaskScreenFragmentImpl
import com.example.joshu.ui.mainScreen.baseTaskScreen.IBaseTaskScreenPresenter
import com.example.joshu.ui.mainScreen.calendar.CalendarScreenPresenterImpl.DataDecoration
import com.example.joshu.ui.mainScreen.calendar.decorators.DecoratorMultipleDots
import com.example.joshu.viewUtils.AppBarStateChangeListenerAbs
import com.example.joshu.viewUtils.PriorityUiUtils
import com.google.android.material.appbar.AppBarLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import kotlinx.android.synthetic.main.fragment_calendar_screen.*
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlin.collections.ArrayList

class CalendarScreenFragmentImpl : BaseTaskScreenFragmentImpl(), ICalendarScreenView {

    @InjectPresenter
    lateinit var presenter: CalendarScreenPresenterImpl

    init {
        Injector.getInstance().appComponent().inject(this)
    }

    @ProvidePresenter
    fun providePresenter(): CalendarScreenPresenterImpl {
        val presenter = CalendarScreenPresenterImpl(strings)
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun getPresenter(): IBaseTaskScreenPresenter = presenter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_calendar_screen, container, false)

    override fun initView() {
        initCalendar()
        initAppBar()
    }

    private fun initCalendar() {
        calendarView.setOnDateChangedListener { _, date, _ ->
            presenter.onDateSelected(CalendarDate(date.day, date.month, date.year))
        }
    }

    private fun initAppBar() {
        appBarView.addOnOffsetChangedListener(object: AppBarStateChangeListenerAbs() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                when (state) {
                    State.EXPANDED -> presenter.onAppBarExpanded()
                    State.COLLAPSED -> presenter.onAppBarCollapsed()
                    State.IDLE -> return
                }
            }
        } )
    }

    override fun createDecoration(dataDecoration: DataDecoration) {
        val decoratorsList = ArrayList<DayViewDecorator>()
        dataDecoration.listForDecoratorOnePriority.forEach {
            decoratorsList.add(DecoratorMultipleDots(
                    it.value,
                    createListColorsFromPriorities(listOf(it.key))))
        }
        dataDecoration.listForDecoratorTwoPriority.forEach {
            decoratorsList.add(DecoratorMultipleDots(
                    it.value, createListColorsFromPriorities(it.key)
            ))
        }
        dataDecoration.listForDecoratorThreePriority.forEach {
            decoratorsList.add(DecoratorMultipleDots(
                    it.value, createListColorsFromPriorities(it.key)
            ))
        }
        decoratorsList.add(DecoratorMultipleDots(
                dataDecoration.listForDecoratorFourTask,
                createListColorsFromPriorities(PriorityTypeEnum.values().toList())))

        calendarView.removeDecorators()
        calendarView.addDecorators(decoratorsList)
    }

    override fun initActionBar(text: String) {
        setToolBar(toolbarView)

        getActionBar()?.apply {
            title = text
        }
    }

    override fun setSelectedDate(date: CalendarDate) {
        val calendarDay = CalendarDay.from(date.year, date.month, date.day)
        calendarView.currentDate = calendarDay
        calendarView.selectedDate = calendarDay
    }

    override fun setMinMaxDateToCalendar(minDate: CalendarDate, maxDate: CalendarDate) {
        calendarView.state().edit()
            .setMinimumDate(createCalendarDayFromCalendarDate(minDate))
            .setMaximumDate(createCalendarDayFromCalendarDate(maxDate))
            .commit()
    }

    private fun createCalendarDayFromCalendarDate(calendarDate: CalendarDate): CalendarDay =
        CalendarDay.from(calendarDate.year, calendarDate.month, calendarDate.day)

    override fun onAppBarCollapsed() {
        val tv = TypedValue()
        if (requireActivity().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            val actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            setTopMarginToRecyclerView(actionBarHeight)
        }
    }

    override fun onAppBarExpanded() {
        setTopMarginToRecyclerView(0)
    }

    private fun setTopMarginToRecyclerView(margin: Int) {
        recyclerView.updateLayoutParams {
            (this as? ViewGroup.MarginLayoutParams)?.topMargin = margin
        }
    }

    private fun createListColorsFromPriorities(priorities: Collection<PriorityTypeEnum>): List<Int> {
        val result = ArrayList<Int>(priorities.size)
        priorities.forEach {
            result.add(ContextCompat.getColor(requireContext(), PriorityUiUtils.getColor(it)))
        }
        return result
    }
}