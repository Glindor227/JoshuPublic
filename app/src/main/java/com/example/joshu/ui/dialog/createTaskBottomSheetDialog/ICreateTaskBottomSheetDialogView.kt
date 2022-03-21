package com.example.joshu.ui.dialog.createTaskBottomSheetDialog

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.joshu.mvp.model.entity.DateRepeatEnum
import com.example.joshu.mvp.model.entity.PriorityTypeEnum
import com.example.joshu.mvp.view.IBaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface ICreateTaskBottomSheetDialogView: IBaseView {
    fun setPriority(priority: PriorityTypeEnum)
    fun setFolder(folderName: String)
    fun setText(text: String)
    fun showSelectPriorityScreen()
    fun showSelectFolderScreen()
    fun showSelectDateTimeScreen(initDateTime: Long, repeatDateTimeType: DateRepeatEnum)
    fun showResultDateTime(dateTime: String)
    fun sendDismissToInteraction()
    fun showSaveChangesDialog()
    fun updateWidgetInfo()
}