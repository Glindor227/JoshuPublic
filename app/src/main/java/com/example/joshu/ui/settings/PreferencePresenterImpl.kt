package com.example.joshu.ui.settings

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.ISharedPreferences
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.api.network.Result
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.mvp.model.entity.room.dao.TaskFolderDao
import com.example.joshu.mvp.model.repo.UserRepo
import com.example.joshu.mvp.presenter.BasePresenterAbs
import com.facebook.login.LoginManager
import com.google.firebase.auth.FacebookAuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@InjectViewState
class PreferencePresenterImpl(private val strings: IStrings) :
    BasePresenterAbs<IPreferenceView>(strings), IPreferencePresenter {

    @Inject
    lateinit var dbFolderDao: TaskFolderDao

    @Inject
    lateinit var sharedPreferences: ISharedPreferences

    @Inject
    lateinit var userRepo: UserRepo

    override fun loadFoldersInfo() {
        val job = scope.launch {
            val folderList = dbFolderDao.getAll()
                .plus(TaskFolder(strings.settingDefaultListId, strings.settingLastUsedList, ""))

            withContext(Dispatchers.Main) {
                viewState.updateFolderDefaultList(folderList)
            }
        }
        addJob(job, "getFoldersFromDB")
    }

    override fun getDefaultFolder() {
        val job = scope.launch {
            val folderId = sharedPreferences.defaultFolderId
            val folderName = if (folderId == strings.settingDefaultListId) {
                strings.settingLastUsedList
            } else {
                dbFolderDao.getById(folderId).title
            }

            withContext(Dispatchers.Main) {
                viewState.updateDefaultTaskListSummary(folderName)
            }
        }
        addJob(job, "getDefaultFolder")
    }

    override fun logout() {
        val job = scope.launch {
            val result = userRepo.logout()

            when (result) {
                is Result.Success -> {
                    FirebaseAuth.getInstance().signOut()
                    LoginManager.getInstance().logOut()
                    sharedPreferences.resetTokens()
                    sharedPreferences.resetAuth()
                    withContext(Dispatchers.Main) {
                        viewState.showAuthAndRegistrationScreen()
                    }
                }
                is Result.Error -> {
                    sharedPreferences.resetTokens()
                    sharedPreferences.resetAuth()
                    withContext(Dispatchers.Main) {
                        viewState.closeScreen()
                    }
                }
            }

        }
        addJob(job, "logout")
    }

    override fun deleteAccount() {
        //TODO("Not yet implemented")
    }

}