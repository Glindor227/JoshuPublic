package com.example.joshu.ui.auth.mainScreen

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.ui.BaseActivityAbs
import com.example.joshu.ui.auth.emailScreen.AuthEmailScreenActivityImpl
import com.example.joshu.ui.auth.regEmailScreen.RegEmailScreenActivityImpl
import com.example.joshu.ui.mainScreen.MainScreenActivityImpl
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth_screen.*
import javax.inject.Inject


class AuthMainScreenActivityImpl : BaseActivityAbs(), IAuthMainScreenView {
    companion object {
        private const val RC_SIGN_IN = 9001

        fun show(context: Context) {
            val intent = Intent(context, AuthMainScreenActivityImpl::class.java)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var strings: IStrings

    @InjectPresenter
    lateinit var presenter: AuthMainScreenPresenterImpl

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    init {
        Injector.getInstance().appComponent().inject(this)
    }

    @ProvidePresenter
    fun providePresenter(): AuthMainScreenPresenterImpl {
        val presenter = AuthMainScreenPresenterImpl(strings)
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_screen)

        auth = Firebase.auth
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    task.getResult(ApiException::class.java)?.idToken?.let {
                        presenter.onSuccessGetGoogleToken(it)
                    }
                } catch (e: ApiException) {
                    presenter.onErrorGoogleSignIn(e)
                }
            }
            else -> {
                callbackManager.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun firebaseAuthWithGoogle(idToken: String) =
        signInFirebase(GoogleAuthProvider.getCredential(idToken, null))

    override fun firebaseAuthWithFacebook(accessToken: String) =
        signInFirebase(FacebookAuthProvider.getCredential(accessToken))

    private fun signInFirebase(credential: AuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.let {
                        presenter.onSuccessSignIn(it.uid, it.displayName)
                    }
                } else {
                    task.exception?.let {
                        presenter.onErrorFacebookSignIn(it)
                    }
                }
            }
    }

    override fun showMainScreen() {
        MainScreenActivityImpl.show(this)
    }

    override fun initView() {
        police_text.paintFlags = police_text.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        val loginEmailBtn = layout_login_email
        if (loginEmailBtn is MaterialButton) {
            loginEmailBtn.backgroundTintList = ContextCompat.getColorStateList(
                this,
                R.color.color_F9520A
            )
            initButton(loginEmailBtn, R.string.auth_main_login, R.drawable.ic_baseline_email_24,
                View.OnClickListener { presenter.loginClick() }
            )
        }

        initButton(
            layout_reg_email, R.string.auth_main_reg, R.drawable.ic_baseline_add_circle_24,
            View.OnClickListener { presenter.registrationClick() }
        )

        initButton(layout_loginGPlus, null, R.drawable.ic_google,
            View.OnClickListener { presenter.loginFbClick() }
        )

        initButton(layout_loginFb, null, R.drawable.ic_facebook,
            View.OnClickListener { presenter.loginGPClick() }
        )

        police_text.setOnClickListener {
            presenter.onClickPolice()
        }
    }

    private fun initButton(
        button: View,
        text: Int?,
        icon: Int?,
        onListener: View.OnClickListener
    ) {
        if (button is MaterialButton) {
            text?.let {
                button.setText(text)
            }
            icon?.let {
                button.icon = ContextCompat.getDrawable(this, it)
                button.iconTint = null
            }
            button.setOnClickListener(onListener)
        }
    }

    override fun initGoogleAuth() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun initFacebookAuth() {
        callbackManager = CallbackManager.Factory.create()
    }

    override fun startCheckCurrentSignIn() {
        auth.currentUser?.let {
            val uid = it.uid
            val displayName = it.displayName

            presenter.onSuccessSignIn(uid, displayName)
        } ?: presenter.noAuthCurrentUser()
    }

    override fun showEMailSingInScreen() {
        AuthEmailScreenActivityImpl.show(this)
    }

    override fun showGPlusSingInScreen() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun showFacebookSingInScreen() {
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    loginResult?.let {
                        presenter.onSuccessGetFacebookToken(it.accessToken.token)
                    }
                }

                override fun onCancel() {
                    presenter.onCancelFacebookSignIn()
                }

                override fun onError(exception: FacebookException) {
                    presenter.onErrorFacebookSignIn(exception)
                }
            })

        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
    }

    override fun showEMailRegistrationScreen() {
        RegEmailScreenActivityImpl.show(this)    }

    override fun showPolice() {
//        TODO("Not yet implemented")
    }
}