package com.example.joshu.ui.auth.mainScreen

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.joshu.mvp.view.IBaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface IAuthMainScreenView: IBaseView {
    fun initGoogleAuth()
    fun initFacebookAuth()
    fun startCheckCurrentSignIn()
    fun showEMailSingInScreen()
    fun showGPlusSingInScreen()
    fun showFacebookSingInScreen()
    fun showEMailRegistrationScreen()
    fun showPolice()

    fun firebaseAuthWithGoogle(idToken: String)
    fun firebaseAuthWithFacebook(accessToken: String)
    fun showMainScreen()
}