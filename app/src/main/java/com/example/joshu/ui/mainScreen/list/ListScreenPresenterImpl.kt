package com.example.joshu.ui.mainScreen.list

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.ISharedPreferences
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.entity.DeleteStatusEnum
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.mvp.model.entity.room.dao.TaskDao
import com.example.joshu.mvp.model.entity.room.dao.TaskFolderDao
import com.example.joshu.mvp.model.repo.ITaskFolderRepo
import com.example.joshu.mvp.presenter.BasePresenterAbs
import com.example.joshu.mvp.presenter.list.BaseListPresenter
import com.example.joshu.mvp.presenter.list.IFolderListPresenter
import com.example.joshu.mvp.view.list.IFolderListRowView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@InjectViewState
class ListScreenPresenterImpl(val strings: IStrings) : BasePresenterAbs<IListScreenView>(strings),
    IListScreenPresenter {

    companion object {
        const val NO_ID = -1
    }

    @Inject
    lateinit var dbTaskDao: TaskDao

    @Inject
    lateinit var dbFolderDao: TaskFolderDao

    @Inject
    lateinit var sharedPreferences: ISharedPreferences

    @Inject
    lateinit var taskFolderRepo: ITaskFolderRepo

    private val cacheTasksCount = HashMap<Int, Int>()
    private val listPresenter: FolderListPresenterImpl
    private var folderToDelete: Int = NO_ID

    private val onClick: (Int, TaskFolder) -> Unit = { _, folder ->
        viewState.openFolder(folder.id)
    }
    private val onLongClick: (Int, TaskFolder) -> Unit = { _, folder ->
        folderToDelete = folder.id
        viewState.showDeleteDialog()
    }

    init {
        listPresenter =
            FolderListPresenterImpl(onClick, onLongClick, cacheTasksCount)
    }

    inner class FolderListPresenterImpl(
        private val onClick: (Int, TaskFolder) -> Unit,
        private val onLongClick: (Int, TaskFolder) -> Unit,
        private val cacheTasksCount: HashMap<Int, Int>
    ) : BaseListPresenter<TaskFolder>(), IFolderListPresenter {

        override fun onLongClick(position: Int) {
            val item = getItemByPosition(position)
            onLongClick.invoke(position, item)
        }

        override fun bind(view: IFolderListRowView) {
            val position = view.getPos()
            val item = getItemByPosition(position)
            view.setTitle(item.title)
            val countOfTasks = cacheTasksCount[item.id] ?: 0
            if (countOfTasks == 0) {
                view.setEmptyIcon()
            } else {
                view.setFilledIcon()
                view.setCountOfTasks(countOfTasks)
            }

            if (isFirstEmptyFolder(position)) {
                view.showDivider()
            } else {
                view.hideDivider()
            }
        }

        override fun onClick(position: Int) {
            val item = getItemByPosition(position)
            onClick.invoke(position, item)
        }

        private fun isFirstEmptyFolder(position: Int): Boolean {
            if (position == 0) {
                return false
            }

            val item = getItemByPosition(position)
            val prevItem = getItemByPosition(position - 1)
            val currentTaskCount = cacheTasksCount[item.id] ?: 0
            val prevTaskCount = cacheTasksCount[prevItem.id] ?: 0
            return currentTaskCount == 0 && prevTaskCount > 0
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.initView()
        viewState.initRecyclerView()
        viewState.hideEmptyView()
        viewState.initActionBar()
    }

    override fun getListPresenter(): IFolderListPresenter = listPresenter

    override fun onResume() {
        super.onResume()
        getFoldersFromDB()
    }

    private fun getFoldersFromDB() {
        val job = scope.launch {
            val folderList = dbFolderDao.getAll()
            val folderIds = folderList.map { it.id }
            updateCacheTasksCount(folderIds)

            val sortedFolderList = folderList.sortedWith(
                Comparator { folder1, folder2 ->
                    val countOfTasks1 = cacheTasksCount[folder1.id] ?: 0
                    val countOfTasks2 = cacheTasksCount[folder2.id] ?: 0
                    if (countOfTasks1 > 0 && countOfTasks2 == 0) {
                        -1
                    } else if (countOfTasks1 == 0 && countOfTasks2 > 0) {
                        1
                    } else {
                        folder1.id.compareTo(folder2.id)
                    }
                }
            )

            listPresenter.list.clear()
            listPresenter.list.addAll(sortedFolderList)

            withContext(Dispatchers.Main) {
                viewState.updateList()
                if (folderIds.isEmpty()) {
                    viewState.showEmptyView()
                } else {
                    viewState.hideEmptyView()
                }
            }

        }
        addJob(job, "getFoldersFromDB")
    }

    private suspend fun updateCacheTasksCount(folderIds: List<Int>) {
        cacheTasksCount.clear()
        if (folderIds.isNotEmpty()) {
            folderIds.forEach {
                val countOfTasks = dbTaskDao.getCountByFolder(it, DeleteStatusEnum.No.value)
                cacheTasksCount[it] = countOfTasks
            }
        }
    }

    private fun deleteFolderFromDB(folderId: Int) {
        val job = scope.launch {
            dbTaskDao.deleteByFolderId(folderId)
            dbFolderDao.deleteById(folderId)
            if (sharedPreferences.defaultFolderId == folderId) {
                sharedPreferences.defaultFolderId = strings.settingDefaultListId
            }
        }.apply {
            invokeOnCompletion {
                getFoldersFromDB()
                viewState.updateWidgetInfo()
            }
        }
        addJob(job, "deleteFolderFromDB")
    }

    override fun onDismissCreateDialog() {
        getFoldersFromDB()
    }

    override fun onConfirmCreateFolderDialog(folderId: Int) {
        viewState.updateList()
        viewState.openFolder(folderId)
    }

    override fun onConfirmDeleteDialog() {
        if (folderToDelete != NO_ID)
            deleteFolderFromDB(folderToDelete)
    }


}