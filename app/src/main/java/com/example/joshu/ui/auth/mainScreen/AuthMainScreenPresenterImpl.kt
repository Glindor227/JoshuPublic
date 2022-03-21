package com.example.joshu.ui.auth.mainScreen

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.INetworkUtils
import com.example.joshu.mvp.model.ISharedPreferences
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.api.network.Result
import com.example.joshu.mvp.model.repo.UserRepo
import com.example.joshu.mvp.presenter.BasePresenterAbs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@InjectViewState
class AuthMainScreenPresenterImpl(private val strings: IStrings)
    : BasePresenterAbs<IAuthMainScreenView>(strings), IAuthMainScreenPresenter {

    @Inject
    lateinit var sharedPreferences: ISharedPreferences
    @Inject
    lateinit var networkUtils: INetworkUtils
    @Inject
    lateinit var userRepo: UserRepo

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initGoogleAuth()
        viewState.initFacebookAuth()
        viewState.initView()

        lockScreen()
        viewState.startCheckCurrentSignIn()
    }

    override fun loginClick() = viewState.showEMailSingInScreen()

    override fun registrationClick() = viewState.showEMailRegistrationScreen()

    override fun loginGPClick() = viewState.showGPlusSingInScreen()

    override fun loginFbClick() = viewState.showFacebookSingInScreen()

    override fun onClickPolice() = viewState.showPolice()

    override fun onErrorGoogleSignIn(e: Throwable) {
        unlockScreen()
        showErrorByThrowable(e)
    }

    override fun onSuccessGetGoogleToken(tokenId: String) {
        lockScreen()
        viewState.firebaseAuthWithGoogle(tokenId)
    }

    override fun onErrorFacebookSignIn(e: Throwable) {
        unlockScreen()
        showErrorByThrowable(e)
    }

    override fun onCancelFacebookSignIn() {
        showMessage(strings.cancelFacebookSignIn)
    }

    override fun onSuccessGetFacebookToken(token: String) {
        lockScreen()
        viewState.firebaseAuthWithFacebook(token)
    }

    override fun onSuccessSignIn(id: String, name: String?) {
        sharedPreferences.userId = id
        sharedPreferences.userDisplayName = name

        if (checkInternetShowError(networkUtils.isInternetOn())) {
            lockScreen()
            val job = scope.launch {
                val result = userRepo.authUserOnServer(id, name)

                withContext(Dispatchers.Main) {
                    when(result) {
                        is Result.Success -> {
                            nextScreen()
                        }
                        is Result.Error -> {
                            showErrorByThrowable(result.exception)
                            nextScreen()
                        }
                    }
                }
            }
            addJob(job, "auth")
        } else {
            nextScreen()
        }
    }

    override fun noAuthCurrentUser() {
        unlockScreen()
    }

    private fun nextScreen() {
        viewState.showMainScreen()
        closeAffinityScreen()
    }
}