package com.example.joshu.ui.mainScreen.list.folder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.ui.mainScreen.baseTaskScreen.BaseTaskScreenFragmentImpl
import com.example.joshu.ui.mainScreen.baseTaskScreen.IBaseTaskScreenPresenter
import kotlinx.android.synthetic.main.fragment_folder_screen.*
import kotlinx.android.synthetic.main.toolbar.*

class FolderScreenFragmentImpl : BaseTaskScreenFragmentImpl(), IFolderScreenView {

    companion object {
        const val FOLDER_ID = "FOLDER_ID"

        fun createBundle(folderId: Int): Bundle {
            val bundle = Bundle()
            bundle.putInt(FOLDER_ID, folderId)
            return bundle
        }
    }

    init {
        Injector.getInstance().appComponent().inject(this)
    }

    @InjectPresenter
    lateinit var presenter: FolderScreenPresenterImpl

    @ProvidePresenter
    fun providePresenter(): FolderScreenPresenterImpl {
        val folderId = arguments?.getInt(FOLDER_ID) ?: 0
        val presenter = FolderScreenPresenterImpl(strings, folderId)
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun getPresenter(): IBaseTaskScreenPresenter = presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_folder_screen, container, false)

    override fun initActionBar() {
        setToolBar(toolbarView)
        getActionBar()?.apply {
            title = getString(R.string.list)
        }
    }

    override fun updateFolderTitle(title: String) {
        folderTitleView.text = title
    }

    override fun updateCountOfTasks(tasksCount: Int) {
        countOfTasksView.text = "$tasksCount"
    }
}