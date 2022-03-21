package com.example.joshu.ui.mainScreen.statistic

import com.arellomobile.mvp.InjectViewState
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.mvp.presenter.BasePresenterAbs

@InjectViewState
class StatisticScreenPresenterImpl(strings: IStrings)
    : BasePresenterAbs<IStatisticScreenView>(strings), IStatisticScreenPresenter {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.initView()
    }
}