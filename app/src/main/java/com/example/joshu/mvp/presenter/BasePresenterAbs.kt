package com.example.joshu.mvp.presenter

import androidx.annotation.CallSuper
import androidx.annotation.UiThread
import com.arellomobile.mvp.MvpPresenter
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.api.network.error.ErrorUiUtils
import com.example.joshu.mvp.view.IBaseView
import com.example.joshu.utils.LoggerHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BasePresenterAbs<T: IBaseView>(private val strings: IStrings)
    : MvpPresenter<T>(), IBasePresenter {

    private val parentJob = Job()
    private val coroutineContext = parentJob + Dispatchers.Default
    protected val scope = CoroutineScope(coroutineContext)

    private val compositeJob = CompositeJob()
    private var hasDestroyView = false

    override fun attachView(view: T) {
        super.attachView(view)

        if (hasDestroyView) {
            onFirstViewAttach()
        }
        hasDestroyView = false
    }

    override fun destroyView(view: T) {
        super.destroyView(view)
        unSubscribe()
        hasDestroyView = true
    }

    @CallSuper
    override fun onStart() {
    }

    @CallSuper
    override fun onResume() {
    }

    @CallSuper
    override fun onPause() {
        unSubscribe()
    }

    @CallSuper
    override fun onStop() {
    }

    protected fun addJob(job: Job, key: String) {
        compositeJob.add(job, key)
    }

    @CallSuper
    protected fun unSubscribe() {
        compositeJob.cancel()
    }

    @UiThread
    protected fun closeScreen() {
        viewState.closeScreen()
    }

    @UiThread
    protected fun closeAffinityScreen() {
        viewState.closeAffinityScreen()
    }

    @UiThread
    protected fun lockScreen() {
        viewState.lockScreen()
    }

    @UiThread
    protected fun unlockScreen() {
        viewState.unlockScreen()
    }

    @UiThread
    protected fun showMessage(message: CharSequence) {
        viewState.showMessage(message)
    }

    @UiThread
    protected fun showError(errorMessage: CharSequence) {
        viewState.showError(errorMessage)
    }

    @UiThread
    protected fun hideSoftKeyboard() {
        viewState.hideSoftKeyboard()
    }

    protected fun checkInternetShowError(isOnline: Boolean): Boolean {
        if (!isOnline) {
            showErrorInternet()
        }
        return isOnline
    }

    protected fun showErrorInternet() {
        showError(strings.internetError)
    }

    protected fun showErrorServer() {
        showError(strings.serverError)
    }

    protected fun showErrorByThrowable(throwable: Throwable) {
        val errorList = ErrorUiUtils.getMessageListByThrowable(throwable, strings)

        for (errorRes in errorList) {
            showError(errorRes)
        }
        LoggerHelper.error(this, "error", throwable, false)
    }
}