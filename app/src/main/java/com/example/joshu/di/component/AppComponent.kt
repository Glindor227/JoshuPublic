package com.example.joshu.di.component

import com.example.joshu.di.module.AppModule
import com.example.joshu.di.module.RepoModule
import com.example.joshu.di.module.RetrofitModule
import com.example.joshu.di.module.UtilsModule
import com.example.joshu.ui.splashScreen.SplashScreenActivityImpl
import com.example.joshu.ui.splashScreen.SplashScreenPresenterImpl
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, RetrofitModule::class, RepoModule::class, UtilsModule::class])
@Singleton
interface AppComponent {
    fun inject(activity: SplashScreenActivityImpl)

    fun inject(presenter: SplashScreenPresenterImpl)
}