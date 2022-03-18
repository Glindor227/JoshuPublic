package com.example.joshu.di.module

import android.content.Context
import com.example.joshu.mvp.model.*
import com.example.joshu.mvp.model.api.IJwtParser
import com.example.joshu.mvp.model.observable.IObservableTask
import com.example.joshu.observable.ObservableTaskImpl
import com.example.joshu.ui.image.FrescoImageLoaderImpl
import com.example.joshu.ui.jwtParser.JwtParserImpl
import com.example.joshu.ui.strings.AndroidStringsByResourcesImpl
import com.example.joshu.ui.validation.ValidationImpl
import com.example.joshu.utils.NetworkUtilsImpl
import com.example.joshu.utils.BufferTaskUtilImpl
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

    @Provides
    @Singleton
    fun provideValidation(): IValidationUtils {
        return ValidationImpl()
    }

    @Provides
    @Singleton
    fun provideJwtParser(): IJwtParser {
        return JwtParserImpl()
    }

    @Provides
    @Singleton
    fun provideBufferTaskUtil(): IBufferTaskUtil {
        return BufferTaskUtilImpl()
    }

    @Provides
    @Singleton
    fun provideObservableTask(): IObservableTask = ObservableTaskImpl()
}

