package com.example.joshu.ui.forgotPasswordByEmailScreen

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.model.IValidationUtils
import com.example.joshu.mvp.presenter.BasePresenterAbs
import java.lang.Exception
import javax.inject.Inject

@InjectViewState
class ForgotPasswordScreenPresenterImpl(private val strings: IStrings)
    : BasePresenterAbs<IForgotByEmailView>(strings), IForgotByEmailPresenter {

    @Inject
    lateinit var validationUtils: IValidationUtils

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initView()
    }

    override fun onClickClose() {
        closeScreen()
    }

    override fun onClickBtnRecovery(email: String?) {
        if (isValidateEmail(email)) {
            lockScreen()
            email?.let {
                viewState.onRecovery(it)
            } ?: unlockScreen()
        } else {
            showError(strings.invalidateEmail)
        }
    }

    override fun sendSuccess() {
        unlockScreen()
        showMessage(strings.recoverySuccess)
        closeScreen()
    }

    override fun authError(exception: Exception) {
        unlockScreen()
        showErrorByThrowable(exception)
    }

    private fun isValidateEmail(email: String?): Boolean {
        return email?.let {
            validationUtils.isValidEmail(it)
        } ?: false
    }
}