package com.example.joshu.ui.auth.emailScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.ui.BaseActivityAbs
import com.example.joshu.ui.forgotPasswordByEmailScreen.ForgotPasswordScreenActivityImpl
import com.example.joshu.ui.mainScreen.MainScreenActivityImpl
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth_email_screen.*
import javax.inject.Inject

class AuthEmailScreenActivityImpl: BaseActivityAbs(), IAuthEmailScreenView {
    companion object {
        fun show(context: Context) {
            val intent = Intent(context, AuthEmailScreenActivityImpl::class.java)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var strings: IStrings

    @InjectPresenter
    lateinit var presenter: AuthEmailScreenPresenterImpl

    private var emailInputEdit: TextInputEditText? = null
    private var passwordInputEdit: TextInputEditText? = null

    init {
        Injector.getInstance().appComponent().inject(this)
    }

    @ProvidePresenter
    fun providePresenter(): AuthEmailScreenPresenterImpl {
        val presenter = AuthEmailScreenPresenterImpl(strings)
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_email_screen)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun showForgotScreen() {
        ForgotPasswordScreenActivityImpl.show(this)
    }

    override fun showAuthEmail(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { itTask ->
                if (itTask.isSuccessful) {
                    itTask.result?.user?.let{
                        presenter.authSuccess(it.uid, it.displayName)
                    } ?: presenter.authUserInfoError()
                } else {
                    itTask.exception?.let {
                        presenter.authError(it)
                    }
                }
            }
    }

    override fun showMainActivity() {
        MainScreenActivityImpl.show(this)
    }

    override fun showMainScreen() {
        MainScreenActivityImpl.show(this)
    }

    override fun initView() {
        closeView.setOnClickListener {
            presenter.onClickClose()
        }

        val emailInputLayout = emailInputLayoutView
        if (emailInputLayout is TextInputLayout) {
            emailInputLayout.hint = getString(R.string.enter_address_email_hint)
            emailInputEdit = emailInputLayout.findViewById(R.id.editTextView)
            emailInputEdit?.inputType = InputType.TYPE_CLASS_TEXT  or
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }

        val passwordInputLayout = passwordInputLayoutView
        if (passwordInputLayout is TextInputLayout) {
            passwordInputLayout.hint = getString(R.string.enter_your_password)
            passwordInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE

            passwordInputEdit = passwordInputLayout.findViewById(R.id.editTextView)
            passwordInputEdit?.inputType = InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        val btn = actionBtnView
        if (btn is MaterialButton) {
            btn.setText(R.string.sign_in)
        }
        btn.setOnClickListener {
            presenter.onClickSignIn(emailInputEdit?.text?.toString()?.trim(),
                passwordInputEdit?.text?.toString())
        }

        forgotPasswordView.setOnClickListener {
            presenter.onClickForgot()
        }
    }
}