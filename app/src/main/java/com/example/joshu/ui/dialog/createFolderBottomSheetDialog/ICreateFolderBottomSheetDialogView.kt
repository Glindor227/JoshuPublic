package com.example.joshu.ui.dialog.createFolderBottomSheetDialog

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.joshu.mvp.view.IBaseView

@StateStrategyType(OneExecutionStateStrategy::class)
interface ICreateFolderBottomSheetDialogView : IBaseView {
    fun sendConfirmToInteraction(folderId: Int)
}