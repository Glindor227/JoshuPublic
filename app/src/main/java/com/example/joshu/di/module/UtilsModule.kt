package com.example.joshu.di.module

import android.content.Context
import com.example.joshu.mvp.model.IImageLoader
import com.example.joshu.mvp.model.INetworkUtils
import com.example.joshu.mvp.model.IStrings
import com.example.joshu.ui.image.FrescoImageLoaderImpl
import com.example.joshu.ui.strings.AndroidStringsByResourcesImpl
import com.example.joshu.utils.NetworkUtilsImpl
import com.facebook.drawee.view.SimpleDraweeView
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilsModule {
    @Provides
    @Singleton
    fun provideNetworkUtils(context: Context): INetworkUtils {
        return NetworkUtilsImpl(context)
    }

    @Provides
    @Singleton
    fun provideFrescoImageLoader(): IImageLoader<SimpleDraweeView> {
        return FrescoImageLoaderImpl()
    }

    @Provides
    @Singleton
    fun provideStrings(context: Context): IStrings {
        return AndroidStringsByResourcesImpl(context)
    }
}

