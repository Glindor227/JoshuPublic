package com.example.joshu.ui.dialog.folderSelected

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.ui.dialog.bottomSheetsDialog.BaseBottomSheetDialogFragmentMoxy
import com.example.joshu.ui.dialog.createFolderBottomSheetDialog.CreateFolderBottomSheetDialogFragmentImpl
import com.example.joshu.ui.dialog.createFolderBottomSheetDialog.ICreateFolderBottomSheetDialogInteraction
import javax.inject.Inject

class FolderSelectedBottomSheetDialogFragmentImpl(private val interaction: ISelectedFolderInteraction)
    : BaseBottomSheetDialogFragmentMoxy(), IFolderSelectedBottomSheetDialogView {

    companion object {
        fun createInstance(interaction: ISelectedFolderInteraction)
                : FolderSelectedBottomSheetDialogFragmentImpl {
            return FolderSelectedBottomSheetDialogFragmentImpl(interaction)
        }
    }

    @Inject
    lateinit var strings: IStrings

    @InjectPresenter
    lateinit var presenter: FolderSelectedBottomSheetDialogPresenterImpl

    @BindView(R.id.createNewFolderView)
    lateinit var createNewFolderView: ViewGroup

    @BindView(R.id.plateForFoldersView)
    lateinit var plateForFoldersView: ViewGroup

    init {
        Injector.getInstance().appComponent().inject(this)
    }

    @ProvidePresenter
    fun providePresenter(): FolderSelectedBottomSheetDialogPresenterImpl {
        val presenter = FolderSelectedBottomSheetDialogPresenterImpl(strings)
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        initCreateDialog(dialog)
        return dialog
    }

    private fun initCreateDialog(dialog: Dialog) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_selected_folder, null)

        ButterKnife.bind(this, view)
        presenter.onInitedCreateDialog()
        dialog.setContentView(view)
    }

    override fun showCreateFolderScreen() {
        requireActivity().supportFragmentManager.let {
            CreateFolderBottomSheetDialogFragmentImpl(object :
                ICreateFolderBottomSheetDialogInteraction {

                override fun onConfirm(folderId: Int) {
                    presenter.onConfirmCreateFolderDialog(folderId)
                }

            }).show(it, "NewFolderDialog")
        }
    }

    override fun showFolders(folderList: List<TaskFolder>) {
        folderList.forEach {
            val view = createFolderView(it)
            plateForFoldersView.addView(view)
        }
    }

    override fun setSelectedFolder(folder: TaskFolder) {
        interaction.selectedFolder(folder)
    }

    private fun createFolderView(folder: TaskFolder): View {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.selected_folder_item, null)

        val imageView = view.findViewById<ImageView>(R.id.iconView)
        val titleView = view.findViewById<TextView>(R.id.titleView)

        imageView.setColorFilter(Color.parseColor(folder.color))
        titleView.text = folder.title

        view.tag = folder
        view.setOnClickListener {
            val item = it.tag as TaskFolder
            presenter.onSelectFolder(item)
        }

        return view
    }

    override fun initView() {
        createNewFolderView.setOnClickListener {
            presenter.onClickCreateFolder()
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
}