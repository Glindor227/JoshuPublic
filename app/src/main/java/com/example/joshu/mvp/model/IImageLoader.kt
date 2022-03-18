package com.example.joshu.mvp.model

import androidx.annotation.DrawableRes

interface IImageLoader<T> {
    fun loadInto(url: String?, container: T, @DrawableRes defaultImage: Int = 0)
}