package com.example.joshu.mvp.view.list

interface IFolderListRowView : IBaseListRowView {
    fun setEmptyIcon()
    fun setFilledIcon()
    fun setTitle(title: String)
    fun setCountOfTasks(countOfTasks: Int)
    fun showDivider()
    fun hideDivider()
}