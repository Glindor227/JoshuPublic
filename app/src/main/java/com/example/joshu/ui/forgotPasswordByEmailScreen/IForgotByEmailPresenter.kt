package com.example.joshu.ui.forgotPasswordByEmailScreen

import com.example.joshu.mvp.presenter.IBasePresenter
import java.lang.Exception

interface IForgotByEmailPresenter: IBasePresenter {
    fun onClickClose()
    fun onClickBtnRecovery(email: String?)
    fun sendSuccess()
    fun authError(exception: Exception)
}