package com.example.joshu.ui.auth.regEmailScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
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
import kotlinx.android.synthetic.main.activity_auth_email_screen.actionBtnView
import kotlinx.android.synthetic.main.activity_auth_email_screen.closeView
import kotlinx.android.synthetic.main.activity_auth_email_screen.emailInputLayoutView
import kotlinx.android.synthetic.main.activity_auth_email_screen.passwordInputLayoutView
import kotlinx.android.synthetic.main.activity_reg_email_screen_impl.*
import kotlinx.android.synthetic.main.input_field_outlinedbox.view.*
import javax.inject.Inject

class RegEmailScreenActivityImpl : BaseActivityAbs(), IRegEmailScreenView {
    companion object {
        fun show(context: Context) {
            val intent = Intent(context, RegEmailScreenActivityImpl::class.java)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var strings: IStrings

    @InjectPresenter
    lateinit var presenter: RegEmailScreenPresenterImpl

    init {
        Injector.getInstance().appComponent().inject(this)
    }
    @ProvidePresenter
    fun providePresenter(): RegEmailScreenPresenterImpl {
        val presenter = RegEmailScreenPresenterImpl(strings)
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_email_screen_impl)
    }

    override fun createAccount(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { itTask ->
                if (itTask.isSuccessful) {
                    Firebase.auth.currentUser?.let {
                        presenter.regSuccess(it.uid, it.displayName)
                    }
                } else {
                    itTask.exception?.let {
                        presenter.regError(it)
                    }
                }
            }
    }

    override fun showMainActivity() {
        MainScreenActivityImpl.show(this)
    }

    private fun initInputTextView(view: View, hint:String, icon:Int?, type:Int):TextInputEditText?{
        var editText:TextInputEditText? = null
        if (view is TextInputLayout) {
            view.hint = hint
            icon?.let { view.endIconMode = it }
            editText = view.editTextView
            editText.inputType = type
        }
        return editText

    }
    override fun initView() {
        closeView.setOnClickListener {
            presenter.onClickClose()
        }
        val emailInputEdit = initInputTextView(emailInputLayoutView,
            getString(R.string.enter_address_email_hint)
            ,null,InputType.TYPE_CLASS_TEXT  or
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        val passwordInputEdit = initInputTextView(passwordInputLayoutView,
            getString(R.string.enter_your_password)
            ,TextInputLayout.END_ICON_PASSWORD_TOGGLE,InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_PASSWORD)
        val passwordConfirmEdit = initInputTextView(passwordRepidLayoutView,
            getString(R.string.repid_your_password)
            ,TextInputLayout.END_ICON_PASSWORD_TOGGLE,InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_PASSWORD)

        val btn = actionBtnView
        if (btn is MaterialButton) {
            btn.setText(R.string.reg_email_btn)
        }
        btn.setOnClickListener {
            presenter.onClickReg(emailInputEdit?.text?.toString()?.trim(),
                passwordInputEdit?.text?.toString(),
                passwordConfirmEdit?.text?.toString()
            )
        }
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
}