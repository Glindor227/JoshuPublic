package com.example.joshu.ui.mainScreen

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.joshu.mvp.view.IBaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface IMainScreenView: IBaseView {
    fun setVoiceIconToBtnAddTask()
    fun setPlusIconToBtnAddTask()
    fun createNewTask(text: String?, autoCreate: Boolean)
    fun showSpeechListener()
    fun initActionBar()
    fun sendDismissActionToFragment()
}