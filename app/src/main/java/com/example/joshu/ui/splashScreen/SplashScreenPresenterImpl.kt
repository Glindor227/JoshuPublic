package com.example.joshu.ui.splashScreen

import android.accounts.AuthenticatorException
import android.util.Log
import android.view.ViewDebug
import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.ISharedPreferences
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.api.network.Result
import com.example.joshu.mvp.model.api.network.error.ServerNotAuthException
import com.example.joshu.mvp.model.api.response.user.TimeZoneResponse
import com.example.joshu.mvp.model.repo.SynchronizeRepo
import com.example.joshu.mvp.model.repo.UserRepo
import com.example.joshu.mvp.presenter.BasePresenterAbs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@InjectViewState
class SplashScreenPresenterImpl(strings: IStrings): BasePresenterAbs<ISplashScreenView>(strings),
    ISplashScreenPresenter {

    @Inject
    lateinit var sharedPreferences: ISharedPreferences
    @Inject
    lateinit var userRepo: UserRepo


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.initView()
        checkNextScreen()
    }

    private fun checkNextScreen() {
        val job = scope.launch {
            delay(200L)
            withContext(Dispatchers.Main) {
                if (sharedPreferences.accessToken.isNullOrEmpty()) {
                    viewState.showAuthAndRegistrationScreen()
                } else {
                    when(val response = refreshToken()) {
                        is Result.Success -> {
                            setTimeZone()
                            viewState.showMainScreen()
                        }
                        is Result.Error -> {
                            viewState.showAuthAndRegistrationScreen()
                        }
                    }
                }
                closeScreen()
            }
        }
        addJob(job, "emulation wait")
    }

    private suspend fun setTimeZone(): Result<TimeZoneResponse> = userRepo.setTimeZone()



    private suspend fun refreshToken(): Result<Unit> = userRepo.refreshToken()
}