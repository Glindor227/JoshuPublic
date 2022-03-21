package com.example.joshu.ui.dialog.bottomSheetsDialog

import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.presenter.BasePresenterAbs
import com.example.joshu.mvp.view.IBaseView

abstract class BaseBottomSheetDialogPresenterAbs<T: IBaseView>(private val strings: IStrings)
    : BasePresenterAbs<T>(strings), IBottomSheetDialogPresenter {

    override fun onInitedCreateDialog() {
    }
}