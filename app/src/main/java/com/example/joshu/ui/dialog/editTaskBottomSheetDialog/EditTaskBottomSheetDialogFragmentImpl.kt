package com.example.joshu.ui.dialog.editTaskBottomSheetDialog

import android.os.Bundle
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.di.Injector
import com.example.joshu.ui.dialog.createTaskBottomSheetDialog.CreateTaskBottomSheetDialogFragmentImpl
import com.example.joshu.utils.ViewsUtil

class EditTaskBottomSheetDialogFragmentImpl : CreateTaskBottomSheetDialogFragmentImpl() {

    companion object {
        private const val TASK_ID = "TASK_ID"

        fun createInstance(taskId: Int): EditTaskBottomSheetDialogFragmentImpl {
            val fragment = EditTaskBottomSheetDialogFragmentImpl()
            val bundle = Bundle()
            bundle.putInt(TASK_ID, taskId)
            fragment.arguments = bundle
            return fragment
        }
    }

    @ProvidePresenter
    override fun providePresenter(): EditTaskBottomSheetDialogPresenterImpl {
        val taskId = requireArguments().getInt(TASK_ID)
        val presenter = EditTaskBottomSheetDialogPresenterImpl(strings, taskId)
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun initView() {
        super.initView()
        ViewsUtil.showViews(taskBlocker)
        ViewsUtil.hideViews(btnSendView)
        newTaskEditTextView?.isFocusableInTouchMode = false

        taskBlocker.setOnClickListener {
            ViewsUtil.goneViews(taskBlocker)
            ViewsUtil.showViews(btnSendView)
            newTaskEditTextView?.isFocusableInTouchMode = true
        }

    }


}