package com.example.joshu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.arellomobile.mvp.MvpAppCompatDialogFragment
import com.arellomobile.mvp.MvpAppCompatFragment
import com.example.joshu.R
import com.example.joshu.mvp.view.IBaseView
import com.example.joshu.utils.ViewsUtil

abstract class BaseFragmentAbs: MvpAppCompatFragment(), IBaseView {
    private var lockScreen: ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        initLockScreen()
    }

    protected fun initLockScreen() {
        lockScreen = LayoutInflater.from(context).inflate(R.layout.lock_screen, null) as ViewGroup
        ViewsUtil.goneViews(lockScreen)
        (activity?.window?.decorView as ViewGroup).addView(lockScreen)
    }

    override fun closeScreen() {
        activity?.finish()
    }

    override fun closeAffinityScreen() {
        activity?.finishAffinity()
    }

    override fun showError(errorMessage: CharSequence) {
        showToast(errorMessage)
    }

    override fun showMessage(message: CharSequence) {
        showToast(message)
    }

    override fun lockScreen() {
        ViewsUtil.showViews(lockScreen)
    }

    override fun unlockScreen() {
        ViewsUtil.goneViews(lockScreen)
    }

    protected fun setToolBar(toolbar: Toolbar) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    /** protected **/


    protected fun getActionBar(): ActionBar? {
        return if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar
        } else {
            null
        }
    }

    protected fun hideActionBar() {
        val actionBar = getActionBar()

        actionBar?.let {
            if (it.isShowing) {
                it.hide()
            }
        }
    }

    protected fun showActionBar() {
        val actionBar = getActionBar()

        actionBar?.let {
            if (!it.isShowing) {
                it.show()
            }
        }
    }

    @CallSuper
    protected fun setCustomActionBar() {
        val actionBar = getActionBar()

        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayShowCustomEnabled(true)
    }

    @CallSuper
    protected fun restoreActionBar() {
        val actionBar = getActionBar()

        actionBar?.setDisplayShowCustomEnabled(false)
        actionBar?.setDisplayShowTitleEnabled(true)
    }

    override fun hideSoftKeyboard() {
        ViewsUtil.hideSoftKeyboard(view)
    }

    private fun showToast(message: CharSequence) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }
}