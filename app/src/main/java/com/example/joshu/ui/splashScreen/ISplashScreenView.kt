package com.example.joshu.ui.splashScreen

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.joshu.mvp.view.IBaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface ISplashScreenView: IBaseView {
    fun showAuthAndRegistrationScreen()
    fun showMainScreen()
}