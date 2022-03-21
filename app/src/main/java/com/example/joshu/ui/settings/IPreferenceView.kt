package com.example.joshu.ui.settings

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.mvp.view.IBaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface IPreferenceView : IBaseView {
    fun updateFolderDefaultList(folders: List<TaskFolder>)
    fun updateDefaultTaskListSummary(folderName: String)
    fun showAuthAndRegistrationScreen()
}