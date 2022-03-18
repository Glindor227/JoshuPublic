package com.example.joshu.ui

import android.os.Bundle
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.example.joshu.mvp.view.IBaseView
import com.example.joshu.utils.ViewsUtil

abstract class BaseActivityAbs: MvpAppCompatActivity(), IBaseView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun closeScreen() {
        finish()
    }

    override fun closeAffinityScreen() {
        finishAffinity()
    }

    override fun lockScreen() {
    }

    override fun hideSoftKeyboard() {
        ViewsUtil.hideSoftKeyboard(window.decorView)
    }

    override fun unlockScreen() {
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