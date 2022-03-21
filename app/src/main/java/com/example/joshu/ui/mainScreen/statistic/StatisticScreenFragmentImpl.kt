package com.example.joshu.ui.mainScreen.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.joshu.R
import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.ui.BaseFragmentAbs
import com.example.joshu.ui.mainScreen.IMainScreenCreateDialogTaskInteraction
import javax.inject.Inject

class StatisticScreenFragmentImpl : BaseFragmentAbs(), IStatisticScreenView,
    IMainScreenCreateDialogTaskInteraction {

    @Inject
    lateinit var strings: IStrings
    @InjectPresenter
    lateinit var presenter: StatisticScreenPresenterImpl

    init {
        Injector.getInstance().appComponent().inject(this)
    }

    @ProvidePresenter
    fun providePresenter(): StatisticScreenPresenterImpl {
        val presenter = StatisticScreenPresenterImpl(strings)
        Injector.getInstance().appComponent().inject(presenter)
        return presenter
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_statistic_screen, container, false)

    override fun initView() {

    }

    override fun onCreateDialogDismiss() {
    }
}