package com.example.joshu.mvp.presenter.list


open class BaseListPresenter<T> {
    val list =  ArrayList<T>()
    fun getItemByPosition(position: Int): T {
        if (position < 0 || position >= list.size) {
            throw IllegalArgumentException("incorrect index position list")
        }
        return list[position]
    }

    fun getCount(): Int {
        return list.size
    }

    fun clear() {
        list.clear()
    }
}