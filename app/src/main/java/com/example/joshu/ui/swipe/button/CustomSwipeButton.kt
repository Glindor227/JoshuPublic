package com.example.joshu.ui.swipe.button

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import com.example.joshu.ui.swipe.listener.ISwipeButtonClickListener
import com.example.joshu.viewUtils.ImageConvert

class CustomSwipeButton(private val context: Context,
                        val typeButtonPosition: ButtonPosition,
                        val width:Int,
                        private val imageId: Int,
                        private val imageBias: Float,
                        private val buttonColor: Int,
                        private val buttonLighterColor: Int,
                        private val action: ButtonActionType,
                        private val listener: ISwipeButtonClickListener?
) {
    companion object {
        const val DEFAULT_TEXT_SIZE = 30
    }

    private var commandSend = false
    private var position = 0
    private var clickRegion: RectF? = null
    @Volatile
    var currentButtonColor: Int = initColor()
    private var text: String? = null
    private var textSize: Float? = null
    private var textOrientation: ButtonViewOrientation? = null

    fun addText(
        text: String,
        textSize: Float?,
        orientation: ButtonViewOrientation
    ): CustomSwipeButton {
        this.text = text
        this.textSize = textSize
        textOrientation = orientation
        return this
    }

    private fun initColor(): Int {
        val codeColor = if (action == ButtonActionType.STANDARD) {
            buttonColor
        } else {
            buttonLighterColor
        }
        return ContextCompat.getColor(context, codeColor)
    }

    fun onUp(x: Float, y: Float): Boolean {
        val hasClick = onActionCommon(x, y, buttonColor)
        if (hasClick) {
            listener?.onClick(position)
        }
        return hasClick
    }

    fun onDown(x: Float, y: Float): Boolean {
        return onActionCommon(x, y, buttonLighterColor)
    }

    private fun onActionCommon(x: Float, y: Float, color:Int): Boolean{
        val localClickRegion = clickRegion ?: return false
        return if (localClickRegion.contains(x, y) && action == ButtonActionType.STANDARD) {
            currentButtonColor = ContextCompat.getColor(context, color)
            true
        } else {
            false
        }
    }

    fun onDraw(canvas: Canvas, rectF: RectF, positionItem: Int): Boolean{
        if (action == ButtonActionType.FAST) {
            onActionFastButton(rectF)
        }
        onDrawLocal(canvas, rectF, currentButtonColor)
        clickRegion = rectF
        position = positionItem
        return commandSend
    }

    private fun onActionFastButton(rectF: RectF) {
        onStartDrawButton(rectF)
        if ((rectF.right - rectF.left >= width) && !commandSend) {
            commandSend = true
            currentButtonColor = ContextCompat.getColor(context, buttonColor)
            listener?.onClick(position)
        }
    }

    private fun onStartDrawButton(rectF: RectF) {
        if (rectF.right == rectF.left) {
            commandSend = false
            currentButtonColor = initColor()
        }
    }

    private fun onDrawLocal(canvas: Canvas, rectF: RectF, colorButton: Int) {
        val paint=Paint()
        paint.color = colorButton
        canvas.drawRect(rectF, paint)


        text?.let {
            paint.color = Color.WHITE
            paint.textSize = textSize ?: DEFAULT_TEXT_SIZE.toFloat()
            if (it.isNotEmpty()) {
                onDrawText(imageId != 0, it, rectF, paint, canvas)
            }
        }
        if(imageId != 0) {
            onDrawImage(text.isNullOrEmpty(), rectF, canvas, paint)
        }
    }

    private fun onDrawText(
        imageExist:Boolean,
        text:String,
        rectF: RectF,
        paint: Paint,
        canvas: Canvas
    ) {
        val textRect = Rect()
        val itemHeight = rectF.height()
        val itemWidth = rectF.width()
        paint.textAlign = Paint.Align.LEFT
        paint.getTextBounds(text, 0, text.length, textRect)
        val textX: Float
        val textY: Float
        textX = itemWidth / 2f - textRect.width() / 2f - textRect.left.toFloat()
        textY = itemHeight / 2f +
                if (!imageExist || textOrientation == ButtonViewOrientation.HORIZONTAL) {
                    textRect.height() / 2f
                } else {
                    - textRect.height() / 2f
                }
        canvas.drawText(text, rectF.left + textX, rectF.top + textY, paint)
    }

    private fun onDrawImage(
        textNotExist: Boolean,
        rectF: RectF,
        canvas: Canvas,
        paint: Paint
    ) {
        if ((rectF.right - rectF.left == width.toFloat()) ||
            (action == ButtonActionType.STANDARD))
        {
            val drawableImage = ContextCompat.getDrawable(context, imageId) ?: return
            val bitmap = ImageConvert.drawableToBitmap(drawableImage)
            val imageBiasByText =
                if (textNotExist || textOrientation == ButtonViewOrientation.HORIZONTAL) {
                - bitmap.height / 2
            } else {
                0
            }
            canvas.drawBitmap(
                bitmap,
                rectF.left + (rectF.right - rectF.left) * imageBias - bitmap.width / 2,
                (rectF.top + rectF.bottom) / 2 + imageBiasByText,
                paint
            )
        }
    }
}
