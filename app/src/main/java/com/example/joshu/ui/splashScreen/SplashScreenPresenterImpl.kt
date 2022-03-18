package com.example.joshu.ui.splashScreen

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.ISharedPreferences
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.presenter.BasePresenterAbs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@InjectViewState
class SplashScreenPresenterImpl(strings: IStrings): BasePresenterAbs<ISplashScreenView>(strings)
    , ISplashScreenPresenter {

    @Inject
    lateinit var sharedPreferences: ISharedPreferences

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.initView()
        checkNextScreen()
    }

    private fun checkNextScreen() {
        val job = scope.launch {
            delay(2 * 1000L)

            withContext(Dispatchers.Main) {
                if (!sharedPreferences.getAccessToken().isNullOrEmpty()) {
                    viewState.showRegistrationScreen()
                } else {
                    viewState.showMainScreen()
                }
                closeScreen()
            }
        }
        addJob(job, "emulation wait")
    }
}