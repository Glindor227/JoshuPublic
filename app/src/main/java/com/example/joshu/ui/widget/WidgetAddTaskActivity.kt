package com.example.joshu.ui.widget

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.joshu.R
import com.example.joshu.ui.dialog.createTaskBottomSheetDialog.CreateTaskBottomSheetDialogFragmentImpl
import com.example.joshu.ui.dialog.editTaskBottomSheetDialog.EditTaskBottomSheetDialogFragmentImpl

class WidgetAddTaskActivity : AppCompatActivity(),
    CreateTaskBottomSheetDialogFragmentImpl.ICreateTaskBottomSheetDialogInteraction {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.widget_activity)
        if (intent.hasExtra(TaskListWidgetProvider.ITEM_ID)) {
            val itemId = intent.getIntExtra(TaskListWidgetProvider.ITEM_ID, 0)
            startEditTaskDialog(itemId)
        } else {
            startNewTaskDialog()
        }
    }

    private fun startNewTaskDialog() {
        val fragmentTransitionImpl = supportFragmentManager.beginTransaction()
        val newTaskDialog =
            CreateTaskBottomSheetDialogFragmentImpl.createIntent(null, false)
        newTaskDialog.show(fragmentTransitionImpl, "NewTaskFragment")
        newTaskDialog.dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun startEditTaskDialog(id: Int) {
        val fragmentTransitionImpl = supportFragmentManager.beginTransaction()
        val newTaskDialog =
            EditTaskBottomSheetDialogFragmentImpl.createInstance(id)
        newTaskDialog.show(fragmentTransitionImpl, "EditTaskFragment")
        newTaskDialog.dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun closeApp() {
        finishAndRemoveTask()
    }

    override fun onDismissCreateTaskBottomSheetDialog() {
        TaskListWidgetProvider.notifyWidgetDataChanged(this)
        closeApp()
    }

}