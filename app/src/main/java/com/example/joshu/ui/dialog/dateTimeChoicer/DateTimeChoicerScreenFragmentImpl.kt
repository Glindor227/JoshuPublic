package com.example.joshu.ui.dialog.dateTimeChoicer

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.entity.DateRepeatEnum
import com.example.joshu.ui.dialog.bottomSheetsDialog.BaseBottomSheetDialogFragmentMoxy
import com.example.joshu.utils.ViewsUtil.goneViews
import com.example.joshu.utils.ViewsUtil.showViews
import com.example.joshu.viewUtils.DateRepeatUiUtils
import com.google.android.material.button.MaterialButton
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.fragment_date_time_choicer.*
import javax.inject.Inject

class DateTimeChoicerScreenFragmentImpl(private val interaction: ISelectedDateScreenInteraction)
    : BaseBottomSheetDialogFragmentMoxy(), OnDateSelectedListener, IDateTimeChoicerScreenView {
    companion object {
        private const val INPUT_TIME = "initTime"
        private const val INPUT_REPEAT_TIME = "initRepeatTime"
        private const val INIT_TIME = 0L
        fun newInstance(initTime: Long,
                        repeatType: Int,
                        interaction: ISelectedDateScreenInteraction
        ) : DateTimeChoicerScreenFragmentImpl =
            DateTimeChoicerScreenFragmentImpl(interaction).apply {
                arguments = Bundle().apply {
                    putLong(INPUT_TIME, initTime)
                    putInt(INPUT_REPEAT_TIME, repeatType)
                }
            }
    }

    @Inject
    lateinit var strings: IStrings

    @InjectPresenter
    lateinit var presenter: DateTimeChoicerScreenPresenterImpl
    init {
        Injector.getInstance().appComponent().inject(this)
    }

    @ProvidePresenter
    fun providePresenter(): DateTimeChoicerScreenPresenterImpl {
        val initTime= arguments?.getLong(INPUT_TIME) ?: INIT_TIME
        val initRepeatTime = arguments?.let {
            DateRepeatEnum.getByValue(it.getInt(INPUT_REPEAT_TIME))
        } ?: DateRepeatEnum.DEFAULT


        val presenter
                = DateTimeChoicerScreenPresenterImpl(strings, initTime, initRepeatTime, interaction)
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.onInitedCreateDialog()
        return inflater.inflate(R.layout.fragment_date_time_choicer, container, false)
    }

    /**
     *  Init block
     */
    override fun initView() {
        initCalendar()
        initTimePicker()
        initButtons()
        initRepeatRadio()
    }

    private fun initRepeatRadio() {
        repeatRadioGroup.removeAllViews()
        DateRepeatEnum.values().forEach {
            val radioButton = RadioButton(requireContext())
            radioButton.text = getString(DateRepeatUiUtils.getTitleStringId(it))
            radioButton.tag = it
            repeatRadioGroup.addView(radioButton)
        }
        repeatRadioGroup.setOnCheckedChangeListener{radioGroup, checkIndex ->
            radioGroup.findViewById<View>(checkIndex)?.let {
                presenter.repeatSelected(it.tag as DateRepeatEnum)
            }
        }
    }

    private fun initButtons() {
        initActionButtons()
        initTextButtons()
    }

    private fun initTextButtons() {
        initButtonTextColor(dateButton, R.color.color_111111)
            .setOnClickListener {
                presenter.dateTextClick(layoutCalendar.visibility == View.VISIBLE)
            }
        initButtonIcon(dateButton, R.drawable.ic_clock)

        initButtonTextColor(timeButton, R.color.color_111111)
            .setOnClickListener {
                presenter.timeTextClick(timePicker.visibility == View.VISIBLE)
            }
        initButtonTextColor(buttonRepeat, R.color.color_111111)
        initButtonIcon(buttonRepeat, R.drawable.ic_repeat)
            .setOnClickListener {
                presenter.repeatTextClick(repeatRadioGroup.visibility == View.VISIBLE)
            }
    }

    private fun initActionButtons() {
        initButtonText(buttonTimeCancel, getString(R.string.cancel))
            .setOnClickListener {
                presenter.timeCancelButtonClick()
            }

        initButtonText(buttonTimeOk, getString(R.string.ok))
            .setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    presenter.timeOkButtonClick(timePicker.hour, timePicker.minute)
                } else {
                    presenter.timeOkButtonClick(timePicker.currentHour, timePicker.currentMinute)
                }
            }
    }

    private fun initTimePicker() {
        timePicker.setIs24HourView(true)
    }

    private fun initCalendar() {
        calendarView.topbarVisible = false
        calendarView.setOnDateChangedListener(this)
    }

    private fun initButtonText(
        button: View,
        text: String
    ): View {
        setButtonText(button,text)
        return button
    }

    private fun setButtonText(
        button: View,
        text: String
    ) {
        (button as? MaterialButton)?.let {
            it.text = text
        } ?: throw IllegalArgumentException(getString(R.string.no_material_button_exception_title))
    }

    private fun initButtonTextColor(
        button: View,
        textColor: Int
    ): View {
        if (button is MaterialButton) {
                button.setTextColor(ContextCompat.getColor(requireContext(), textColor))
        }
        return button
    }

    private fun initButtonIcon(
        button: View,
        icon: Int
    ): View {
        if (button is MaterialButton) {
            button.icon = ContextCompat.getDrawable(requireContext(), icon)
        }
        return button
    }

    /**
     *  implementation interface OnDateSelectedListener
     */
    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        if (selected) {
            presenter.dateSelected(date.year, date.month, date.day)
        }
    }

    /**
     *  implementation interface IDateTimeChoicerScreenView
     */
    override fun hideCalendarVisible() {
        goneViews(layoutCalendar)
    }

    override fun showCalendarVisible() {
        showViews(layoutCalendar)
    }

    override fun hideRepeatVisible() {
        goneViews(repeatRadioGroup)
    }

    override fun showRepeatVisible() {
        showViews(repeatRadioGroup)
    }

    override fun hideTimeVisible() {
        goneViews(timePicker)
    }

    override fun showTimeVisible() {
        showViews(timePicker)
    }

    override fun showDate(date: String) = setButtonText(dateButton, date)

    override fun showTime(time: String) = setButtonText(timeButton, time)

    override fun showRepeat(repeatType: DateRepeatEnum) {
        setButtonText(buttonRepeat, getString(DateRepeatUiUtils.getTitleStringId(repeatType)))

        val radioButton = repeatRadioGroup.children.find {
            repeatType == it.tag as DateRepeatEnum
        }
        radioButton?.let {
            (it as RadioButton).isChecked = true
        }
    }

    override fun setSelectedDate(year: Int, month: Int, day: Int, hours: Int, minute: Int) {
        calendarView.selectedDate = CalendarDay.from(year,  month,  day)
        with(timePicker){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.hour = hours
                this.minute = minute
            } else {
                this.setCurrentHour(hours)
                this.setCurrentMinute(minute)
            }
        }
    }

    /**
     *  implementation fragment interface
     */
    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }
}