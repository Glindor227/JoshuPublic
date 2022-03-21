package com.example.joshu.ui.dialog.createTaskBottomSheetDialog

import com.example.joshu.mvp.model.entity.PriorityTypeEnum
import com.example.joshu.ui.dialog.bottomSheetsDialog.IBottomSheetDialogPresenter

interface ICreateTaskBottomSheetDialogPresenter: IBottomSheetDialogPresenter {
    fun onCloseClick()
    fun onFolderClick()
    fun onPriorityClick()
    fun onClockClick()
    fun onSendClick(text: String?)
    fun setPriorityType(priorityType: PriorityTypeEnum)
    fun onDismiss()
    fun onInputTextChanged(charSequence: CharSequence?)
    fun onOutsideClick()
}