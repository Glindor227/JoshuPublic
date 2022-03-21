package com.example.joshu.ui.dialog.bottomSheetsDialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.MvpAppCompatDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog

open class BottomSheetDialogFragmentMoxy: MvpAppCompatDialogFragment() {
    /**
     * Tracks if we are waiting for a dismissAllowingStateLoss or a regular dismiss once the
     * BottomSheet is hidden and onStateChanged() is called.
     */
    private var waitingForDismissAllowingStateLoss = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
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
            val behavior: BottomSheetBehavior<*> = baseDialog.behavior
            if (behavior.isHideable && baseDialog.dismissWithAnimation) {
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
//            if (dialog is BottomSheetDialog) {
//                dialog?.removeDefaultCallback()
//            }
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
}