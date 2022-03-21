package com.example.joshu.ui.dialog.bottomSheetsDialog

import android.widget.Toast
import com.example.joshu.mvp.view.IBaseView
import com.example.joshu.utils.ViewsUtil


abstract class BaseBottomSheetDialogFragmentMoxy: BottomSheetDialogFragmentMoxy(), IBaseView {
    override fun closeScreen() {
        dismiss()
    }

    override fun closeAffinityScreen() {
        dismiss()
    }

    override fun lockScreen() {
    }

    override fun hideSoftKeyboard() {
        ViewsUtil.hideSoftKeyboard(view)
    }

    override fun unlockScreen() {
    }

    override fun showMessage(message: CharSequence) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(errorMessage: CharSequence) {
        showMessage(errorMessage)
    }
}