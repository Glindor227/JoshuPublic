package com.example.joshu.mvp.presenter

interface IBasePresenter {
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
}