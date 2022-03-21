package com.example.joshu.ui.auth.emailScreen

import com.example.joshu.mvp.presenter.IBasePresenter

interface IAuthEmailScreenPresenter: IBasePresenter {
    fun onClickClose()
    fun onClickSignIn(email: String?, password: String?)
    fun onClickForgot()
    fun authSuccess(userId: String, displayName: String?)
    fun authError(exception: Exception)
    fun authUserInfoError()
}