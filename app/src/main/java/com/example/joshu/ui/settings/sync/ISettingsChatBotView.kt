package com.example.joshu.ui.settings.sync

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.joshu.mvp.view.IBaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface ISettingsChatBotView : IBaseView {
    fun addChatBot()
}