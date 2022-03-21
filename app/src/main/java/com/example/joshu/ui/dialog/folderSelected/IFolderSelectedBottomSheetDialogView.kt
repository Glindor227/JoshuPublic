package com.example.joshu.ui.dialog.folderSelected

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.mvp.view.IBaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface IFolderSelectedBottomSheetDialogView: IBaseView {
    fun showCreateFolderScreen()
    fun showFolders(folderList: List<TaskFolder>)
    fun setSelectedFolder(folder: TaskFolder)
}