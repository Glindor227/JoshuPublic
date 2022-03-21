package com.example.joshu.ui.auth.emailScreen

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.INetworkUtils
import com.example.joshu.mvp.model.ISharedPreferences
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.IValidationUtils
import com.example.joshu.mvp.model.api.network.Result
import com.example.joshu.mvp.model.repo.UserRepo
import com.example.joshu.mvp.presenter.BasePresenterAbs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@InjectViewState
class AuthEmailScreenPresenterImpl(private val strings: IStrings)
    : BasePresenterAbs<IAuthEmailScreenView>(strings), IAuthEmailScreenPresenter {

    @Inject
    lateinit var userRepo: UserRepo
    @Inject
    lateinit var sharedPreferences: ISharedPreferences
    @Inject
    lateinit var validationUtils: IValidationUtils
    @Inject
    lateinit var networkUtils: INetworkUtils

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initView()
    }

    override fun onClickClose() {
        closeScreen()
    }

    override fun onClickSignIn(email: String?, password: String?) {
        if (!isValidEmail(email)) {
            showError(strings.invalidateEmail)
            return
        }

        if (!isValidPassword(password)) {
            showError(strings.invalidatePassword)
            return
        }

        val mEmail = email ?: return
        val mPassword = password ?: return

        viewState.showAuthEmail(mEmail, mPassword)
    }

    private fun isValidEmail(email: String?): Boolean {
        return email?.let {
            validationUtils.isValidEmail(it)
        } ?: false
    }

    private fun isValidPassword(password: String?): Boolean {
        return password?.let {
            validationUtils.isValidPassword(it)
        } ?: false
    }

    override fun onClickForgot() {
        viewState.showForgotScreen()
    }

    override fun authSuccess(userId: String, displayName: String?) {
        sharedPreferences.userId = userId
        sharedPreferences.userDisplayName = displayName

        if (checkInternetShowError(networkUtils.isInternetOn())) {
            lockScreen()
            val job = scope.launch {
                val result = userRepo.authUserOnServer(userId, displayName)

                withContext(Dispatchers.Main) {
                    unlockScreen()
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
        }
    }

    private fun nextScreen() {
        viewState.showMainScreen()
        closeAffinityScreen()
    }

    override fun authError(exception: Exception) {
        showError(exception.toString())
    }

    override fun authUserInfoError() {
        showError(strings.authUserInfoError)
    }
}