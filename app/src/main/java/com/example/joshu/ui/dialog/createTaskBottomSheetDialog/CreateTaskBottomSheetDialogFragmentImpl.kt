package com.example.joshu.ui.dialog.createTaskBottomSheetDialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import butterknife.BindView
import butterknife.ButterKnife
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.entity.DateRepeatEnum
import com.example.joshu.mvp.model.entity.PriorityTypeEnum
import com.example.joshu.ui.dialog.bottomSheetsDialog.BaseBottomSheetDialogFragmentMoxy
import com.example.joshu.ui.dialog.dateTimeChoicer.DateTimeChoicerScreenFragmentImpl
import com.example.joshu.ui.dialog.folderSelected.FolderSelectedBottomSheetDialogFragmentImpl
import com.example.joshu.ui.dialog.messageDialog.IMessageDialogInteraction
import com.example.joshu.ui.dialog.messageDialog.MessageDialogFragmentImpl
import com.example.joshu.ui.widget.TaskListWidgetProvider
import com.example.joshu.utils.ViewsUtil
import com.example.joshu.viewUtils.PriorityUiUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

open class CreateTaskBottomSheetDialogFragmentImpl :
    BaseBottomSheetDialogFragmentMoxy(), ICreateTaskBottomSheetDialogView {

    companion object {
        const val KEY_TEXT_FOR_AUTO_CREATE = "textForAutoCreate"
        const val KEY_BOOLEAN_AUTO_CREATE = "autoCreate"

        fun createIntent(text: String?, autoCreate: Boolean): CreateTaskBottomSheetDialogFragmentImpl {
            val bundle = Bundle()
            text?.let {
                bundle.putString(KEY_TEXT_FOR_AUTO_CREATE, it)
            }
            bundle.putBoolean(KEY_BOOLEAN_AUTO_CREATE, autoCreate)
            val dialog = CreateTaskBottomSheetDialogFragmentImpl()
            dialog.arguments = bundle
            return dialog
        }
    }

    interface ICreateTaskBottomSheetDialogInteraction {
        fun onDismissCreateTaskBottomSheetDialog()
    }

    private var interaction: ICreateTaskBottomSheetDialogInteraction? = null

    @Inject
    lateinit var strings: IStrings

    @InjectPresenter
    lateinit var presenter: CreateTaskBottomSheetDialogPresenterImpl

    @BindView(R.id.btn_close)
    lateinit var btnCloseView: AppCompatImageView

    @BindView(R.id.btn_priority)
    lateinit var btnPriorityView: AppCompatImageView

    @BindView(R.id.btn_clock)
    lateinit var btnClockView: AppCompatImageView

    @BindView(R.id.timeView)
    lateinit var textClockView: TextView

    @BindView(R.id.btn_folder)
    lateinit var btnFolderView: AppCompatImageView

    @BindView(R.id.folderNameView)
    lateinit var folderNameView: TextView

    @BindView(R.id.btn_send)
    lateinit var btnSendView: AppCompatImageView

    @BindView(R.id.new_task_text)
    lateinit var newTaskTextView: TextInputLayout

    @BindView(R.id.edit_task_blocker)
    lateinit var taskBlocker: LinearLayout

    protected var newTaskEditTextView: TextInputEditText? = null

    init {
        Injector.getInstance().appComponent().inject(this)
    }

    @ProvidePresenter
    open fun providePresenter(): CreateTaskBottomSheetDialogPresenterImpl {
        val arrg = arguments ?: throw IllegalArgumentException("Arguments is empty")
        val text = arrg.getString(KEY_TEXT_FOR_AUTO_CREATE)
        val autoCreate = arrg.getBoolean(KEY_BOOLEAN_AUTO_CREATE)

        val presenter =
            CreateTaskBottomSheetDialogPresenterImpl(
                text,
                autoCreate,
                strings
            )
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        initCreateDialog(dialog)
        registerForContextMenu(btnPriorityView)
        return dialog
    }

    private fun initCreateDialog(dialog: Dialog) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_new_task, null)

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

        val outsideZone = dialog.findViewById<View>(com.google.android.material.R.id.touch_outside)
        outsideZone.setOnClickListener {
            presenter.onOutsideClick()
        }
    }

    private fun showPopup(view: View) {
        val popupMenu = PopupMenu(context, view)

        PriorityTypeEnum.values().forEach { priorityTypeEnum ->
            val bitmap = ContextCompat.getDrawable(
                requireContext(),
                PriorityUiUtils.getResourceImage(priorityTypeEnum)
            )?.toBitmap(ViewsUtil.dpToPx(12), ViewsUtil.dpToPx(12))
            val itemMenu = popupMenu.menu.add(
                Menu.NONE,
                priorityTypeEnum.value,
                priorityTypeEnum.value,
                PriorityUiUtils.getName(priorityTypeEnum)
            )
            itemMenu.icon = BitmapDrawable(resources, bitmap)
        }

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            presenter.setPriorityType(PriorityTypeEnum.getByValue(item.itemId))
            true
        })

        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popupMenu)
            mPopup.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception) {

        } finally {
            popupMenu.show()
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

    override fun setPriority(priority: PriorityTypeEnum) {
        btnPriorityView.setImageResource(PriorityUiUtils.getResourceImage(priority))
    }

    override fun setFolder(folderName: String) {
        btnFolderView.setImageResource(R.drawable.ic_folder_selected)
        folderNameView.text = folderName
    }

    override fun setText(text: String) {
        newTaskEditTextView?.setText(text)
        newTaskEditTextView?.setSelection(text.length)
    }

    override fun showSelectPriorityScreen() {
        showPopup(btnPriorityView)
    }

    override fun showSelectFolderScreen() {
        activity?.supportFragmentManager?.let {
            FolderSelectedBottomSheetDialogFragmentImpl
                .createInstance(presenter)
                .show(it, "FolderSelectedBottomSheetDialogFragmentImpl")
        }
    }

    override fun showSelectDateTimeScreen(initDateTime: Long, repeatType: DateRepeatEnum) {
        activity?.supportFragmentManager?.let {
            DateTimeChoicerScreenFragmentImpl
                .newInstance(initDateTime, repeatType.value, presenter)
                .show(it, "DateTimeDialog")
        }
    }

    override fun showResultDateTime(dateTime: String) {
        textClockView.text = dateTime
        btnClockView.setImageResource(R.drawable.ic_clock_complite)
    }

    override fun sendDismissToInteraction() {
        interaction?.onDismissCreateTaskBottomSheetDialog()
    }

    override fun initView() {
        newTaskTextView.hint = getString(R.string.task)
        editTextConfigure(newTaskTextView.findViewById(R.id.editTextView))

        btnCloseView.setOnClickListener {
            presenter.onCloseClick()
        }
        btnPriorityView.setOnClickListener {
            presenter.onPriorityClick()
        }
        btnClockView.setOnClickListener {
            presenter.onClockClick()
        }
        textClockView.setOnClickListener {
            presenter.onClockClick()
        }
        btnFolderView.setOnClickListener {
            presenter.onFolderClick()
        }
        folderNameView.setOnClickListener {
            presenter.onFolderClick()
        }
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

            view.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(editable: Editable?) {
                    presenter.onInputTextChanged(editable?.toString()?.trim())
                }
            })
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ICreateTaskBottomSheetDialogInteraction) {
            interaction = context
        }
    }

    override fun showSaveChangesDialog(){
        requireActivity().supportFragmentManager.let {
            MessageDialogFragmentImpl(strings.taskSaveChanges,
                object : IMessageDialogInteraction {
                    override fun onCancel() {
                        closeScreen()
                    }

                    override fun onConfirm() {
                        presenter.onSendClick(newTaskEditTextView?.text.toString().trim())
                    }
                }).show(it, "saveChangesDialog")
        }
    }

    override fun updateWidgetInfo() {
    }
}