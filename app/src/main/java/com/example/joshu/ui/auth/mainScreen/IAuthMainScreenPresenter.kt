package com.example.joshu.ui.auth.mainScreen

import com.example.joshu.mvp.presenter.IBasePresenter

interface IAuthMainScreenPresenter: IBasePresenter {
    fun loginClick()
    fun registrationClick()
    fun loginGPClick()
    fun loginFbClick()
    fun onClickPolice()

    fun onErrorGoogleSignIn(e: Throwable)
    fun onSuccessGetGoogleToken(tokenId: String)

    fun onErrorFacebookSignIn(e: Throwable)
    fun onCancelFacebookSignIn()
    fun onSuccessGetFacebookToken(token: String)

    fun onSuccessSignIn(id: String, name: String?)
    fun noAuthCurrentUser()
}