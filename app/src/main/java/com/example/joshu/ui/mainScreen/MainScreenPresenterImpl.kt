package com.example.joshu.ui.mainScreen

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.repo.SynchronizeRepo
import com.example.joshu.mvp.presenter.BasePresenterAbs
import kotlinx.coroutines.launch
import javax.inject.Inject

@InjectViewState
class MainScreenPresenterImpl(private val strings: IStrings)
    : BasePresenterAbs<IMainScreenView>(strings), IMainScreenPresenter {

    @Inject
    lateinit var synchronizeRepo: SynchronizeRepo

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        val job = scope.launch {
            synchronizeRepo.synchronize()
        }

        viewState.initActionBar()
        viewState.initView()
        addJob(job, "synchronize")
    }

    override fun onLongClickAddTask() {
        viewState.setVoiceIconToBtnAddTask()
        viewState.showSpeechListener()
    }

    override fun setResultSpeechText(texts: ArrayList<String>?) {
        texts?.let{
            if (it.isNotEmpty()) {
                viewState.setPlusIconToBtnAddTask()
                viewState.createNewTask(it[0], true)
            } else {
                setFailedResultSpeechText()
            }
        } ?: setFailedResultSpeechText()
    }

    override fun setFailedResultSpeechText() {
        viewState.setPlusIconToBtnAddTask()
        showError(strings.errorGetSpeechText)
    }

    override fun setCanceledResultSpeechText() {
        viewState.setPlusIconToBtnAddTask()
    }

    override fun onClickAddNewTask() {
        viewState.createNewTask(null, false)
    }

    override fun onDismissCreateDialog() {
        viewState.sendDismissActionToFragment()
    }
}