package com.example.joshu.ui.auth.regEmailScreen

interface IRegEmailScreenPresenter {
    fun onClickClose()
    fun onClickReg(email: String?, password: String?, confirm: String?)
    fun regSuccess(id: String, displayName: String?)
    fun regError(exception: Exception)

}