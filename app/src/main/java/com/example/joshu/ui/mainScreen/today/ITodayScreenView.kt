package com.example.joshu.ui.mainScreen.today

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.joshu.ui.mainScreen.baseTaskScreen.IBaseTaskScreenView

@StateStrategyType(OneExecutionStateStrategy::class)
interface ITodayScreenView : IBaseTaskScreenView {
    fun initActionBar(text: String)
    fun updateMenuItems()
}