package com.example.joshu.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatDialogFragment
import com.example.joshu.R
import com.example.joshu.mvp.view.IBaseView
import com.example.joshu.utils.ViewsUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.lang.reflect.InvocationTargetException

abstract class BaseBottomSheetDialogFragment: MvpAppCompatDialogFragment(), IBaseView {
    /**
     * Tracks if we are waiting for a dismissAllowingStateLoss or a regular dismiss once the
     * BottomSheet is hidden and onStateChanged() is called.
     */
    private var waitingForDismissAllowingStateLoss = false
    private var lockScreen: ViewGroup? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        initLockScreen()
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    protected fun initLockScreen() {
        lockScreen = LayoutInflater.from(context).inflate(R.layout.lock_screen, null) as ViewGroup
        ViewsUtil.goneViews(lockScreen)
        (activity?.window?.decorView as ViewGroup).addView(lockScreen)
    }

    override fun dismiss() {
        if (!tryDismissWithAnimation(false)) {
            super.dismiss()
        }
    }

    override fun dismissAllowingStateLoss() {
        if (!tryDismissWithAnimation(true)) {
            super.dismissAllowingStateLoss()
        }
    }

    /**
     * Tries to dismiss the dialog fragment with the bottom sheet animation. Returns true if possible,
     * false otherwise.
     */
    private fun tryDismissWithAnimation(allowingStateLoss: Boolean): Boolean {
        val baseDialog = dialog
        if (baseDialog is BottomSheetDialog) {
            val dialog = baseDialog
            val behavior: BottomSheetBehavior<*> = dialog.behavior
            if (behavior.isHideable && dialog.dismissWithAnimation) {
                dismissWithAnimation(behavior, allowingStateLoss)
                return true
            }
        }
        return false
    }

    private fun dismissWithAnimation(
        behavior: BottomSheetBehavior<*>, allowingStateLoss: Boolean
    ) {
        waitingForDismissAllowingStateLoss = allowingStateLoss
        if (behavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            dismissAfterAnimation()
        } else {
            if (dialog is BottomSheetDialog) {
                val dialog = dialog as BottomSheetDialog?
                removeDefaultCallback(dialog)
            }
            behavior.addBottomSheetCallback(BottomSheetDismissCallback())
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
    }

    private fun dismissAfterAnimation() {
        if (waitingForDismissAllowingStateLoss) {
            super.dismissAllowingStateLoss()
        } else {
            super.dismiss()
        }
    }

    inner class BottomSheetDismissCallback : BottomSheetCallback() {
        override fun onStateChanged(
            bottomSheet: View,
            newState: Int
        ) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismissAfterAnimation()
            }
        }

        override fun onSlide(
            bottomSheet: View,
            slideOffset: Float
        ) {
        }
    }

    private fun removeDefaultCallback(instance: BottomSheetDialog?) {
        val bottomSheetDialogClass: Class<*> = BottomSheetDialog::class.java
        try {
            val method =
                bottomSheetDialogClass.getDeclaredMethod("removeDefaultCallback", null)
            method.isAccessible = true
            method.invoke(instance, null)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
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

    override fun hideSoftKeyboard() {
        ViewsUtil.hideSoftKeyboard(view)
    }

    private fun showToast(message: CharSequence) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }
}