package com.example.joshu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import com.arellomobile.mvp.MvpAppCompatActivity
import com.example.joshu.R
import com.example.joshu.mvp.view.IBaseView
import com.example.joshu.utils.ViewsUtil

abstract class BaseActivityAbs: MvpAppCompatActivity(), IBaseView {
    private var lockScreen: ViewGroup? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLockScreen()
    }

    protected fun initLockScreen() {
        lockScreen = LayoutInflater.from(this).inflate(R.layout.lock_screen, null) as ViewGroup
        ViewsUtil.goneViews(lockScreen)
        (window.decorView as ViewGroup).addView(lockScreen)
    }

    override fun closeScreen() {
        finish()
    }

    override fun closeAffinityScreen() {
        finishAffinity()
    }

    override fun lockScreen() {
        ViewsUtil.showViews(lockScreen)
    }

    override fun unlockScreen() {
        ViewsUtil.goneViews(lockScreen)
    }

    override fun hideSoftKeyboard() {
        ViewsUtil.hideSoftKeyboard(window.decorView)
    }

    override fun showMessage(message: CharSequence) {
        showToast(message)
    }

    override fun showError(errorMessage: CharSequence) {
        showToast(errorMessage)
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}