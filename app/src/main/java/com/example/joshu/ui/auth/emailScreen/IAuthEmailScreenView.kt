package com.example.joshu.ui.auth.emailScreen

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.joshu.mvp.view.IBaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface IAuthEmailScreenView: IBaseView {
    fun showForgotScreen()
    fun showAuthEmail(email: String, password: String)
    fun showMainActivity()

    fun showMainScreen()
}