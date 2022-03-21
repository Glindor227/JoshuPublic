package com.example.joshu.ui.mainScreen.baseTaskScreen

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrognito.flashbar.Flashbar
import com.example.joshu.R
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.entity.TaskStatusEnum
import com.example.joshu.ui.BaseFragmentAbs
import com.example.joshu.ui.adapter.TaskListRvAdapter
import com.example.joshu.ui.dialog.dateTimeChoicer.DateTimeChoicerScreenFragmentImpl
import com.example.joshu.ui.dialog.editTaskBottomSheetDialog.EditTaskBottomSheetDialogFragmentImpl
import com.example.joshu.ui.mainScreen.IMainScreenCreateDialogTaskInteraction
import com.example.joshu.ui.swipe.button.ButtonActionType
import com.example.joshu.ui.swipe.button.ButtonPosition
import com.example.joshu.ui.swipe.button.ButtonViewOrientation
import com.example.joshu.ui.swipe.button.CustomSwipeButton
import com.example.joshu.ui.swipe.helper.SwipeHelper
import com.example.joshu.ui.swipe.listener.ISwipeButtonClickListener
import com.example.joshu.ui.widget.TaskListWidgetProvider
import com.example.joshu.utils.ViewsUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.view_task_list.*
import javax.inject.Inject


abstract class BaseTaskScreenFragmentImpl : BaseFragmentAbs(), IBaseTaskScreenView,
    IMainScreenCreateDialogTaskInteraction {

    companion object {
        const val TASK_DELETE_TIMEOUT = 3000
        const val SWIPE_BUTTON_SMALL_WIDTH = 150
        const val SWIPE_BUTTON_LARGE_WIDTH = 600
        const val IMAGE_BIAS_MIDDLE = 0.5F
        const val IMAGE_BIAS_END = 0.8F
        const val IMAGE_BIAS_BEGIN = 0.2F
    }

    @Inject
    lateinit var strings: IStrings

    protected lateinit var swipeHelper: SwipeHelper

    private var adapter: TaskListRvAdapter? = null

    abstract fun getPresenter(): IBaseTaskScreenPresenter

    override fun onStart() {
        super.onStart()
        getPresenter().onStart()
    }

    override fun onResume() {
        super.onResume()
        getPresenter().onResume()
    }

    override fun onPause() {
        super.onPause()
        getPresenter().onPause()
    }

    override fun onStop() {
        super.onStop()
        getPresenter().onStop()
    }

    override fun initView() {

    }

    override fun initRecyclerView() {
        val recyclerViewManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = recyclerViewManager
        adapter = TaskListRvAdapter(getPresenter().getListPresenter())
        recyclerView.adapter = adapter
        initSwipe()
    }

    private fun initSwipe() {
        swipeHelper = object : SwipeHelper(
            requireContext(),
            recyclerView,
            SWIPE_BUTTON_SMALL_WIDTH
        ) {
            override fun addButtons(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<CustomSwipeButton>
            ) {
                val holder = viewHolder as TaskListRvAdapter.ViewHolderTask
                var taskStatus: TaskStatusEnum = TaskStatusEnum.DEFAULT
                holder.statusTask?.let {
                    taskStatus = TaskStatusEnum.getByValue(it)
                }
                with(buffer) {
                    if (taskStatus != TaskStatusEnum.Wait) {
                        add(
                            CustomSwipeButton( //Send
                                requireContext(), ButtonPosition.RIGHT,
                                SWIPE_BUTTON_SMALL_WIDTH,
                                R.drawable.ic_swipe_send,
                                IMAGE_BIAS_MIDDLE,
                                R.color.color_253CF1, R.color.color_676EE3_80,
                                ButtonActionType.STANDARD,
                                object : ISwipeButtonClickListener {
                                    override fun onClick(pos: Int) =
                                        getPresenter().onSendSwipeButtonClick(pos)
                                }
                            )
                        )
                    }
                    add(
                        CustomSwipeButton(//Calendar
                            requireContext(), ButtonPosition.RIGHT,
                            SWIPE_BUTTON_SMALL_WIDTH,
                            R.drawable.ic_swipe_later,
                            IMAGE_BIAS_MIDDLE,
                            R.color.color_F9B60A, R.color.color_F9B60A_80,
                            ButtonActionType.STANDARD,
                            object : ISwipeButtonClickListener {
                                override fun onClick(pos: Int) =
                                    getPresenter().onCalendarSwipeButtonClick(pos)
                            }
                        )
                    )
                    add(
                        CustomSwipeButton( //Delete
                            requireContext(), ButtonPosition.RIGHT,
                            SWIPE_BUTTON_SMALL_WIDTH,
                            R.drawable.ic_swipe_delete_24,
                            IMAGE_BIAS_MIDDLE,
                            R.color.color_F9520A, R.color.color_F9520A_80,
                            ButtonActionType.STANDARD,
                            object : ISwipeButtonClickListener {
                                override fun onClick(pos: Int) =
                                    getPresenter().onDeleteSwipeButtonClick(pos)
                            }
                        )
                    )
                    val leftButtonText = if (taskStatus == TaskStatusEnum.Doing) {
                        getString(R.string.chip_finish)
                    } else {
                        getString(R.string.chip_doing)
                    }
                    val leftButtonColor = if (taskStatus == TaskStatusEnum.Doing) {
                        R.color.color_253CF1
                    } else {
                        R.color.color_53B885
                    }
                    val leftButtonLighterColor = if (taskStatus == TaskStatusEnum.Doing) {
                        R.color.color_676EE3_80
                    } else {
                        R.color.color_53B885_80
                    }
                    add(
                        CustomSwipeButton(//Doing
                            requireContext(), ButtonPosition.LEFT,
                            SWIPE_BUTTON_LARGE_WIDTH,
                            R.drawable.ic_swipe_check_24,
                            IMAGE_BIAS_BEGIN,
                            leftButtonColor, leftButtonLighterColor,
                            ButtonActionType.FAST,
                            object : ISwipeButtonClickListener {
                                override fun onClick(pos: Int) =
                                    getPresenter().onDoingSwipeButtonClick(pos, taskStatus)
                            }
                        ).addText(
                            leftButtonText,
                            context?.resources?.getDimension(R.dimen.swipe_button_text_height),
                            ButtonViewOrientation.HORIZONTAL
                        )
                    )
                }
            }
        }
    }


    override fun updateList() {
        adapter?.notifyDataSetChanged()
    }

    override fun enableEmptyView(enabled: Boolean) {
        if (enabled) {
            ViewsUtil.showViews(emptyView)
        } else {
            ViewsUtil.goneViews(emptyView)
        }
    }

    override fun onCreateDialogDismiss() {
        getPresenter().onDismissCreateDialog()
    }

    override fun postponeTask(oldDateTime: Long, repeatType: Int) {
        parentFragmentManager.let {
            DateTimeChoicerScreenFragmentImpl
                .newInstance(oldDateTime, repeatType, getPresenter())
                .show(it, "DateTimeDialog")

        }
    }

    override fun deleteTask(taskName: String) {
        Flashbar.Builder(requireActivity())
            .gravity(Flashbar.Gravity.BOTTOM)
            .duration(TASK_DELETE_TIMEOUT.toLong())
            .backgroundColorRes(R.color.black_85)
            .message(getString(R.string.task_delete) + " " + taskName)
            .showProgress(Flashbar.ProgressPosition.LEFT)
            .primaryActionText(getString(R.string.cancel))
            .primaryActionTextColorRes(R.color.color_4757EC)
            .primaryActionTapListener(object : Flashbar.OnActionTapListener {
                override fun onActionTapped(bar: Flashbar) {
                    getPresenter().onDeleteCancelClicked()
                    bar.dismiss()
                }
            })
            .barDismissListener(object : Flashbar.OnBarDismissListener {
                override fun onDismissing(bar: Flashbar, isSwiped: Boolean) {
                }

                override fun onDismissProgress(bar: Flashbar, progress: Float) {
                }

                override fun onDismissed(bar: Flashbar, event: Flashbar.DismissEvent) {
                    if (event == Flashbar.DismissEvent.TIMEOUT) {
                        getPresenter().onDeleteFinished()
                    }
                }
            })
            .build()
            .show()
    }

    override fun sendTask() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.send_task_message))
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                // Respond to negative button press
            }
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                getPresenter().onSendOkClicked()
            }
            .show()
    }


    override fun onEditTaskDialog(taskId: Int) {
        val fragmentTransitionImpl = requireActivity().supportFragmentManager.beginTransaction()
        val newTaskDialog = EditTaskBottomSheetDialogFragmentImpl.createInstance(taskId)
        newTaskDialog.show(fragmentTransitionImpl, "EditTaskFragment")
        newTaskDialog.dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun updateWidgetInfo() {
        TaskListWidgetProvider.notifyWidgetDataChanged(this.requireContext())
    }
}