package com.example.joshu.viewUtils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

object ImageConvert {
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if(drawable is BitmapDrawable)
            return drawable.bitmap
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val bitmapCanvas = Canvas(bitmap)
        drawable.setBounds(0, 0, bitmapCanvas.width, bitmapCanvas.height)
        drawable.draw(bitmapCanvas)
        return bitmap
    }

}