package com.example.joshu.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface IBaseView: MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun initView()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun closeScreen()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun closeAffinityScreen()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun lockScreen()
    @StateStrategyType(SkipStrategy::class)
    fun hideSoftKeyboard()
    @StateStrategyType(SkipStrategy::class)
    fun unlockScreen()
    @StateStrategyType(SkipStrategy::class)
    fun showMessage(message: CharSequence)
    @StateStrategyType(SkipStrategy::class)
    fun showError(errorMessage: CharSequence)
}