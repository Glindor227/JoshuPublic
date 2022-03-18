package com.example.joshu.mvp.presenter.list

import com.example.joshu.mvp.view.list.IBaseListRowView

interface IBaseListPresenter<T: IBaseListRowView> {
    fun getCount(): Int
    fun bind(view: T)
    fun onClick(position: Int)
}