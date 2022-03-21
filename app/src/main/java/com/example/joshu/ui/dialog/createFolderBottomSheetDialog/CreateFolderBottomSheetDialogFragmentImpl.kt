package com.example.joshu.ui.dialog.createFolderBottomSheetDialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.ui.dialog.bottomSheetsDialog.BaseBottomSheetDialogFragmentMoxy
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class CreateFolderBottomSheetDialogFragmentImpl(private val interaction: ICreateFolderBottomSheetDialogInteraction) :
    BaseBottomSheetDialogFragmentMoxy(), ICreateFolderBottomSheetDialogView {

    @Inject
    lateinit var strings: IStrings

    @InjectPresenter
    lateinit var presenter: CreateFolderBottomSheetDialogPresenterImpl

    @BindView(R.id.btn_send)
    lateinit var btnSendView: AppCompatImageView

    @BindView(R.id.new_folder_text)
    lateinit var newFolderTextView: TextInputLayout
    private var newTaskEditTextView: TextInputEditText? = null

    init {
        Injector.getInstance().appComponent().inject(this)
    }

    @ProvidePresenter
    fun providePresenter(): CreateFolderBottomSheetDialogPresenterImpl {
        val presenter =
            CreateFolderBottomSheetDialogPresenterImpl(
                strings
            )
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun sendConfirmToInteraction(folderId: Int) {
        interaction.onConfirm(folderId)
    }

    override fun initView() {
        newFolderTextView.hint = getString(R.string.new_folder_hint)
        editTextConfigure(newFolderTextView.findViewById(R.id.editTextView))
        btnSendView.setOnClickListener {
            presenter.onSendClick(newTaskEditTextView?.text.toString().trim())
        }
    }

    private fun editTextConfigure(view: View) {
        if (view is TextInputEditText) {
            newTaskEditTextView = view

            newTaskEditTextView?.apply {
                maxLines = 2
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        initCreateDialog(dialog)
        return dialog
    }

    private fun initCreateDialog(dialog: Dialog) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_new_folder, null)

        ButterKnife.bind(this, view)
        presenter.onInitedCreateDialog()
        dialog.setContentView(view)
        dialog.setOnShowListener {
            dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                ?.let {
                    val behavior = BottomSheetBehavior.from(it)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    behavior.peekHeight = 0
                }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        presenter.onDismiss()
    }

}