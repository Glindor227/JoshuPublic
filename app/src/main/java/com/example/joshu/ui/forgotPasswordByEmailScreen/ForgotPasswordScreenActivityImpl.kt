package com.example.joshu.ui.forgotPasswordByEmailScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.ui.BaseActivityAbs
import com.example.joshu.ui.mainScreen.MainScreenActivityImpl
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_forgot_password.*
import javax.inject.Inject


class ForgotPasswordScreenActivityImpl: BaseActivityAbs(), IForgotByEmailView {
    companion object {
        fun show(context: Context) {
            val intent = Intent(context, ForgotPasswordScreenActivityImpl::class.java)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var strings: IStrings

    @InjectPresenter
    lateinit var presenter: ForgotPasswordScreenPresenterImpl

    private var textInputEdit: TextInputEditText? = null


    init {
        Injector.getInstance().appComponent().inject(this)
    }

    @ProvidePresenter
    fun providePresenter(): ForgotPasswordScreenPresenterImpl {
        val presenter = ForgotPasswordScreenPresenterImpl(strings)
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
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

    override fun onRecovery(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { itTask ->
                if (itTask.isSuccessful) {
                    presenter.sendSuccess()
                } else {
                    itTask.exception?.let {
                        presenter.authError(it)
                    }
                }
            }
    }

    override fun showMainScreen() {
        MainScreenActivityImpl.show(this)
    }

    override fun initView() {
        closeView.setOnClickListener {
            presenter.onClickClose()
        }

        val textInputLayout = textInputLayoutView
        if (textInputLayout is TextInputLayout) {
            textInputLayout.hint = getString(R.string.email)
            textInputEdit =
                textInputLayout.findViewById(R.id.editTextView)
            textInputEdit?.inputType = InputType.TYPE_CLASS_TEXT  or
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }

        val actionBtn = actionBtnView
        if (actionBtn is MaterialButton) {
            actionBtn.setText(R.string.forgot_btn_text)
        }
        actionBtnView.setOnClickListener {
            presenter.onClickBtnRecovery(textInputEdit?.text?.toString()?.trim())
        }
    }

    override fun showMessage(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(errorMessage: CharSequence) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}