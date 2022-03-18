package com.example.joshu.ui.image

import androidx.annotation.DrawableRes
import com.example.joshu.mvp.model.IImageLoader
import com.facebook.drawee.view.SimpleDraweeView

class FrescoImageLoaderImpl: IImageLoader<SimpleDraweeView> {
    override fun loadInto(url: String?, container: SimpleDraweeView, @DrawableRes defaultImage: Int) {
        if (defaultImage != 0) {
            container.hierarchy.setPlaceholderImage(defaultImage)
        }
        container.setImageURI(url, null)
    }
}