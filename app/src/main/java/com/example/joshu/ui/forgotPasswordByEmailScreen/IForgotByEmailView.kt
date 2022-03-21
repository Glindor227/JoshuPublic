package com.example.joshu.ui.forgotPasswordByEmailScreen

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.joshu.mvp.view.IBaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface IForgotByEmailView: IBaseView {
    fun onRecovery(email: String)
    fun showMainScreen()
}
