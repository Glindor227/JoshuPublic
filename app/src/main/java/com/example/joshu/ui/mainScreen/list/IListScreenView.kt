package com.example.joshu.ui.mainScreen.list

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.joshu.mvp.view.IBaseView
import com.example.joshu.ui.widget.WidgetInfoInteraction

@StateStrategyType(OneExecutionStateStrategy::class)
interface IListScreenView : IBaseView, WidgetInfoInteraction {
    fun initRecyclerView()
    fun updateList()
    fun showEmptyView()
    fun hideEmptyView()
    fun initActionBar()
    fun showDeleteDialog()
    fun openFolder(folderId: Int)
}