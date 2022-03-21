package com.example.joshu.ui.dialog.dateTimeChoicer

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.entity.DateRepeatEnum
import com.example.joshu.ui.dialog.bottomSheetsDialog.BaseBottomSheetDialogPresenterAbs
import com.example.joshu.ui.formatter.DateFormatterImpl
import java.util.*

@InjectViewState
class DateTimeChoicerScreenPresenterImpl(private val strings: IStrings,
                                         time: Long,
                                         private var repeatType: DateRepeatEnum,
                                         private val interaction: ISelectedDateScreenInteraction)
    : BaseBottomSheetDialogPresenterAbs<IDateTimeChoicerScreenView>(strings), IDateTimeChoicerScreenPresenter {

    private val dateTimeFormatter = DateFormatterImpl()

    private var calendar  = Calendar.getInstance()
    init {
        if (time != 0L) {
            calendar.timeInMillis = time
        }
    }

    override fun onInitedCreateDialog() {
        viewState.initView()
        viewState.hideTimeVisible()
    }

    override fun onResume() {
        super.onResume()
        sendToViewEventDate()
        sendToViewEventTime()
        viewState.showRepeat(repeatType)
        viewState.setSelectedDate(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )
    }

    private fun sendToViewEventDate() =
        viewState.showDate(dateTimeFormatter.getDateFullOfCalendar(calendar))

    private fun sendToViewEventTime() =
        viewState.showTime(dateTimeFormatter.getTimeShortOfCalendar(calendar))

    override fun dateSelected(year: Int, month: Int, day: Int) {
        with(calendar) {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
        }
        sendToViewEventDate()
    }

    override fun repeatSelected(repeatType: DateRepeatEnum) {
        this.repeatType = repeatType
        viewState.showRepeat(repeatType)
    }

    override fun timeTextClick(visible:Boolean) {
        if (visible){
            viewState.hideTimeVisible()
        } else {
            viewState.showTimeVisible()
            viewState.hideCalendarVisible()
            viewState.hideRepeatVisible()
        }
    }

    override fun dateTextClick(visible:Boolean) {
        if(visible){
            viewState.hideCalendarVisible()
        } else {
            viewState.showCalendarVisible()
            viewState.hideTimeVisible()
            viewState.hideRepeatVisible()
        }
    }

    override fun repeatTextClick(visible: Boolean) {
        if(visible){
            viewState.hideRepeatVisible()
        } else {
            viewState.showRepeatVisible()
            viewState.hideTimeVisible()
            viewState.hideCalendarVisible()

        }
    }

    override fun timeOkButtonClick(hours: Int, minute: Int) {
        with(calendar) {
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, minute)
            interaction.setDateTimeTask(timeInMillis, repeatType)
        }
        sendToViewEventTime()
        viewState.closeScreen()
    }

    override fun timeCancelButtonClick() {
        viewState.closeScreen()
    }
}