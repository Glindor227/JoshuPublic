package com.example.joshu

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.joshu.di.Injector
import com.example.joshu.BuildConfig
import timber.log.Timber

class AppApplication: Application() {
    companion object {
        lateinit var instance: AppApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        instance = this

        initTimber()
        Injector.getInstance().init(this)
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    private class CrashReportingTree: Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }

            t?.let {
                when (priority) {
                    Log.ERROR -> {
                        Log.e(tag, message, it)
                    }
                    Log.WARN -> {
                        Log.w(tag, message, it)
                    }
                    else -> {
                        Log.i(tag, message, it)
                    }
                }
            }
        }
    }
}