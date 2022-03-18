package com.example.joshu.di.module

import android.content.Context
import android.content.SharedPreferences
import com.example.joshu.mvp.model.Constants
import com.example.joshu.ui.sharedPreferences.SharedPreferencesImpl
import com.example.joshu.mvp.model.ISharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val appContext: Context) {
    @Provides
    @Singleton
    fun provideContext(): Context {
        return appContext
    }

    @Provides
    @Singleton
    fun provideSharedPref(): SharedPreferences {
        return appContext.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(sharedPreferences: SharedPreferences): ISharedPreferences {
        return SharedPreferencesImpl(
            sharedPreferences
        )
    }
}