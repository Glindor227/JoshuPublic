package com.example.joshu.ui.auth.regEmailScreen

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
import javax.inject.Inject

@InjectViewState
class RegEmailScreenPresenterImpl(private val strings: IStrings)
    : BasePresenterAbs<IRegEmailScreenView>(strings), IRegEmailScreenPresenter {

    @Inject
    lateinit var validationUtils: IValidationUtils
    @Inject
    lateinit var sharedPreferences: ISharedPreferences
    @Inject
    lateinit var networkUtils: INetworkUtils
    @Inject
    lateinit var userRepo: UserRepo

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initView()
    }

    override fun onClickClose() {
        closeScreen()
    }

    private fun isValidEmail(email: String?): Boolean =
        email?.let {
            validationUtils.isValidEmail(it)
        } ?: false

    private fun isValidPassword(password: String?): Boolean =
        password?.let {
            validationUtils.isValidPassword(it)
        } ?: false

    override fun onClickReg(email: String?, password: String?, confirm: String?) {
        if (!isValidEmail(email)) {
            showError(strings.invalidateEmail)
            return
        }

        if (!isValidPassword(password)) {
            showError(strings.invalidatePassword)
            return
        }
        if (!isValidPassword(password)) {
            showError(strings.invalidatePassword)
            return
        }
        if (!password.equals(confirm)){
            showError(strings.notMatchPassword)
            return
        }
        lockScreen()
        viewState.createAccount(email!!, password!!)
    }

    override fun regSuccess(id: String, displayName: String?) {
        unlockScreen()
        sharedPreferences.userId = id
        sharedPreferences.userDisplayName = displayName

        if (checkInternetShowError(networkUtils.isInternetOn())) {
            lockScreen()
            val job = scope.launch {
                val result = userRepo.authUserOnServer(id, displayName)

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
        viewState.showMainActivity()
        closeAffinityScreen()
    }

    override fun regError(exception: Exception) {
        unlockScreen()
        showError(exception.toString())
    }
}