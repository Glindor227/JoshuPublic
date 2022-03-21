package com.example.joshu.ui.mainScreen.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.ui.BaseFragmentAbs
import com.example.joshu.ui.adapter.FolderListRvAdapter
import com.example.joshu.ui.dialog.createFolderBottomSheetDialog.CreateFolderBottomSheetDialogFragmentImpl
import com.example.joshu.ui.dialog.createFolderBottomSheetDialog.ICreateFolderBottomSheetDialogInteraction
import com.example.joshu.ui.dialog.messageDialog.IMessageDialogInteraction
import com.example.joshu.ui.dialog.messageDialog.MessageDialogFragmentImpl
import com.example.joshu.ui.mainScreen.IMainScreenCreateDialogTaskInteraction
import com.example.joshu.ui.mainScreen.list.folder.FolderScreenFragmentImpl
import com.example.joshu.ui.widget.TaskListWidgetProvider
import com.example.joshu.utils.ViewsUtil
import kotlinx.android.synthetic.main.fragment_list_screen.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class ListScreenFragmentImpl : BaseFragmentAbs(), IListScreenView,
    IMainScreenCreateDialogTaskInteraction {
    @Inject
    lateinit var strings: IStrings

    @InjectPresenter
    lateinit var presenter: ListScreenPresenterImpl

    init {
        Injector.getInstance().appComponent().inject(this)
    }

    @ProvidePresenter
    fun providePresenter(): ListScreenPresenterImpl {
        val presenter = ListScreenPresenterImpl(strings)
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_list_screen, container, false)

    override fun initRecyclerView() {
        val recyclerViewManager = LinearLayoutManager(requireContext())
        foldersRecyclerView.layoutManager = recyclerViewManager
        foldersRecyclerView.adapter = FolderListRvAdapter(presenter.getListPresenter())
    }

    override fun updateList() {
        foldersRecyclerView.adapter?.notifyDataSetChanged()
    }

    override fun showEmptyView() {
        ViewsUtil.showViews(emptyListView)
        ViewsUtil.goneViews(foldersRecyclerView)
    }

    override fun hideEmptyView() {
        ViewsUtil.goneViews(emptyListView)
        ViewsUtil.showViews(foldersRecyclerView)
    }

    override fun initActionBar() {
        setToolBar(toolbarView)
        getActionBar()?.apply {
            title = getString(R.string.list)
        }
    }

    override fun showDeleteDialog() {
        val fragmentTransitionImpl =
            (activity as FragmentActivity).supportFragmentManager.beginTransaction()
        val deleteFolderDialog =
            MessageDialogFragmentImpl(strings.folderDeleteDialogMessage,
                object : IMessageDialogInteraction {
                    override fun onCancel() {
                    }

                    override fun onConfirm() {
                        presenter.onConfirmDeleteDialog()
                    }
                })
        deleteFolderDialog.show(fragmentTransitionImpl, "DeleteFolderFragment")
        deleteFolderDialog.dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun openFolder(folderId: Int) {
        val navController = activity?.findNavController(R.id.nav_host_fragment)
        navController?.navigate(R.id.folder_screen, FolderScreenFragmentImpl.createBundle(folderId))
    }

    override fun initView() {
        addFolder.setOnClickListener {
            if (activity != null && activity is FragmentActivity) {
                val fragmentTransitionImpl =
                    (activity as FragmentActivity).supportFragmentManager.beginTransaction()
                val newFolderDialog =
                    CreateFolderBottomSheetDialogFragmentImpl(object :
                        ICreateFolderBottomSheetDialogInteraction {
                        override fun onConfirm(folderId: Int) {
                            presenter.onConfirmCreateFolderDialog(folderId)
                        }
                    })
                newFolderDialog.show(fragmentTransitionImpl, "NewFolderFragment")
                newFolderDialog.dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
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

    override fun onCreateDialogDismiss() {
        presenter.onDismissCreateDialog()
    }

    override fun updateWidgetInfo() {
        TaskListWidgetProvider.notifyWidgetDataChanged(this.requireContext())
    }
}