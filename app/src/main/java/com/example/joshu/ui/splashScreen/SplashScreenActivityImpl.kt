package com.example.joshu.ui.splashScreen

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.ui.BaseActivityAbs
import com.example.joshu.ui.mainScreen.MainActivity
import javax.inject.Inject

class SplashScreenActivityImpl: BaseActivityAbs(), ISplashScreenView {
    @Inject
    lateinit var strings: IStrings
    @InjectPresenter
    lateinit var presenter: SplashScreenPresenterImpl

    init {
        Injector.getInstance().appComponent().inject(this)
    }

    @ProvidePresenter
    fun providePresenter(): SplashScreenPresenterImpl {
        val presenter = SplashScreenPresenterImpl(strings)
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
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

    override fun showRegistrationScreen() {
//        TODO("Not yet implemented")
    }

    override fun showMainScreen() {
        MainActivity.show(this)
    }

    override fun initView() {
//      TODO("Not yet implemented")
    }
}