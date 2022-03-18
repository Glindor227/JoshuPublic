package com.example.joshu.di

import android.content.Context
import com.example.joshu.di.component.AppComponent
import com.example.joshu.di.component.DaggerAppComponent
import com.example.joshu.di.module.AppModule

class Injector {
    companion object {
        private val instance = Injector()

        fun getInstance(): Injector {
            return instance
        }
    }

    private var appComponent: AppComponent? = null

    fun init(context: Context) {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(context))
            .build()
    }

    fun appComponent(): AppComponent {
        checkAppComponent()
        return appComponent!!
    }

    fun clearAppComponent() {
        appComponent = null
    }

    private fun checkAppComponent() {
        if (appComponent == null) {
            throw NullPointerException("AppComponent need Injector.init()")
        }
    }
}