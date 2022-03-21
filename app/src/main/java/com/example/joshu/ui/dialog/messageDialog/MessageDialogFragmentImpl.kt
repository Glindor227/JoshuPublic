package com.example.joshu.ui.dialog.messageDialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.arellomobile.mvp.MvpAppCompatDialogFragment
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.IStrings
import javax.inject.Inject

class MessageDialogFragmentImpl(private val message: String,
                                private val interaction: IMessageDialogInteraction)
    : MvpAppCompatDialogFragment() {

    @Inject
    lateinit var strings: IStrings

    init {
        Injector.getInstance().appComponent().inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage(message)
        dialogBuilder.setNegativeButton(
            strings.dialogNegative
        ) { _, _ -> interaction.onCancel() }
        dialogBuilder.setPositiveButton(
            strings.dialogPositive
        ) { _, _ -> interaction.onConfirm() }
        return dialogBuilder.create()

    }

}