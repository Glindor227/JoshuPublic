package com.example.joshu.ui.dialog.createFolderBottomSheetDialog

import com.example.joshu.ui.dialog.bottomSheetsDialog.IBottomSheetDialogPresenter

interface ICreateFolderBottomSheetDialogPresenter : IBottomSheetDialogPresenter {
    fun onSendClick(text: String?)
    fun onDismiss()
}