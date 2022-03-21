package com.example.joshu.ui.auth.regEmailScreen

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.joshu.mvp.view.IBaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface IRegEmailScreenView: IBaseView {
    fun createAccount(email: String, password: String)
    fun showMainActivity()
}