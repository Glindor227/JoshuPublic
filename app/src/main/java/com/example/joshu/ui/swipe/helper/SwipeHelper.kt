package com.example.joshu.ui.swipe.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.joshu.ui.swipe.button.ButtonPosition
import com.example.joshu.ui.swipe.button.CustomSwipeButton
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min

abstract class SwipeHelper(context:Context,
                           private val recyclerView: RecyclerView,
                           private var buttonWidth: Int)
    :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT + ItemTouchHelper.RIGHT) {

    companion object {
        const val ESCAPE_VELOCITY_FACTOR = 0.1F
        const val VELOCITY_THRESHOLD_FACTOR = 0.5F
        const val NOT_SWIPE = -1
    }

    private var currentPos: Int = NOT_SWIPE
    private var currentButtonType = ButtonPosition.LEFT
    private var buttonList: MutableList<CustomSwipeButton>? = null
    private lateinit var gestureDetector: GestureDetector
    private var swipePosition = NOT_SWIPE
    private var swipeThreshold = VELOCITY_THRESHOLD_FACTOR
    private val buttonBuffer: MutableMap<Int, MutableList<CustomSwipeButton>> = HashMap()
    private lateinit var removerQueue: LinkedList<Int>

    abstract fun addButtons(viewHolder: RecyclerView.ViewHolder, buffer: MutableList<CustomSwipeButton>)

     private val gestureListener = object: GestureDetector.SimpleOnGestureListener() {

         override fun onLongPress(e: MotionEvent) {
             onUp(e)
         }

         override fun onSingleTapUp(e: MotionEvent) = onUp(e)

         override fun onDoubleTapEvent(e: MotionEvent) = onUp(e)

         override fun onDown(e: MotionEvent): Boolean {
             val swipeButtonList = buttonBuffer[swipePosition] ?:return false
             for (button in swipeButtonList) {
                if (button.onDown(e.x, e.y)) {
                    break
                }
             }
             recyclerView.invalidate()
             return true
         }

         private fun onUp(e: MotionEvent): Boolean {
             val swipeButtonList = buttonBuffer[swipePosition] ?:return false
             for (button in swipeButtonList) {
                 if (button.onUp(e.x, e.y)) {
                     break
                 }
             }
             recyclerView.invalidate()
             return true
         }
     }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener { _, motionEvent ->
        onUpNotify(motionEvent)
        if (swipePosition == NOT_SWIPE) return@OnTouchListener false
        val point = Point(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())
        val swipeItem = recyclerView.findViewHolderForAdapterPosition(swipePosition)?.itemView
        val rect = Rect()
        swipeItem?.getGlobalVisibleRect(rect)
        if (motionEvent.action != MotionEvent.ACTION_DOWN &&
                motionEvent.action != MotionEvent.ACTION_MOVE &&
                motionEvent.action != MotionEvent.ACTION_UP) {
            return@OnTouchListener false
        }
        if (rect.top < point.y && rect.bottom > point.y) {
            gestureDetector.onTouchEvent(motionEvent)
        } else {
            removerQueue.add(swipePosition)
            swipePosition = NOT_SWIPE
            recoverSwipeItem()
        }
        false
    }

    private fun onUpNotify(motionEvent: MotionEvent) {
        if ((motionEvent.action == MotionEvent.ACTION_UP) &&
            (currentButtonType == ButtonPosition.LEFT)
        ) {
            recyclerView.adapter?.notifyItemChanged(currentPos)
        }
    }

    init {
        buttonList = ArrayList()
        gestureDetector = GestureDetector(context, gestureListener)
        recyclerView.setOnTouchListener(onTouchListener)
        removerQueue = LinkedList()
        attachSwipe()
    }

    @Synchronized
    private fun recoverSwipeItem() {
        while (!removerQueue.isEmpty()) {
            val recoverPos = removerQueue.poll() ?: continue
            if (recoverPos != NOT_SWIPE) {
                recyclerView.adapter?.notifyItemChanged(recoverPos)
            }
        }
    }


    public fun clearSwipe() = buttonBuffer.clear()
    private fun attachSwipe() = ItemTouchHelper(this).attachToRecyclerView(recyclerView)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if (swipePosition != pos) {
            removerQueue.add(swipePosition)
        }
        swipePosition = pos
        if (buttonBuffer.containsKey(swipePosition)) {
            buttonList = buttonBuffer[swipePosition]
        } else {
            buttonList?.clear()
        }
        buttonList?.let {
            swipeThreshold = VELOCITY_THRESHOLD_FACTOR * it.size.toFloat() * buttonWidth.toFloat()
        }
        recoverSwipeItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) =  swipeThreshold

    override fun getSwipeEscapeVelocity(defaultValue: Float) = ESCAPE_VELOCITY_FACTOR * defaultValue

    override fun getSwipeVelocityThreshold(defaultValue: Float) =  VELOCITY_THRESHOLD_FACTOR * defaultValue

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        currentPos = viewHolder.adapterPosition
        if (currentPos < 0) {
            swipePosition = currentPos
            return
        }
        setCurrentButtonType(dX)
        val translationX: Float = if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            onSwipeDraw(viewHolder, dX, canvas)
        } else {
            dX
        }
        super.onChildDraw(
            canvas, recyclerView, viewHolder,
            translationX, dY, actionState, isCurrentlyActive
        )
    }

    private fun setCurrentButtonType(dX: Float) {
        currentButtonType = if (dX > 0) {
            ButtonPosition.LEFT
        } else {
            ButtonPosition.RIGHT
        }
    }

    private fun onSwipeDraw(
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        canvas: Canvas
    ): Float {
        val translationX: Float
        var buffer: MutableList<CustomSwipeButton> = ArrayList()
        if (!buttonBuffer.containsKey(currentPos)) {
            addButtons(viewHolder, buffer)
            buttonBuffer[currentPos] = buffer
        } else {
            val tempList = buttonBuffer[currentPos]
            tempList?.let { buffer = it }
        }
        translationX = if (dX < 0F) {
            max(dX, -getTotalWightButtonOfPosition(buffer, ButtonPosition.RIGHT))
        } else {
            min(dX, getTotalWightButtonOfPosition(buffer, ButtonPosition.LEFT))
        }
        drawButtons(canvas, viewHolder.itemView, buffer, currentPos, translationX)
        return translationX
    }

    private fun drawButtons(canvas: Canvas,
                            itemView: View,
                            buffer: MutableList<CustomSwipeButton>,
                            pos: Int,
                            translationX: Float
    ): Boolean {
        var begin: Float
        val currentPosition = if (translationX < 0L) {
            begin = itemView.right.toFloat()
            ButtonPosition.RIGHT

        } else {
            begin = itemView.left.toFloat()
            ButtonPosition.LEFT
        }
        val buttonSwipingWidth = translationX / getCountButtonOfPosition(buffer, currentPosition)
        for (button in buffer) {
            if (button.typeButtonPosition != currentPosition)
                continue
            val end = begin + buttonSwipingWidth
            button.onDraw(
                canvas,
                RectF( min(end, begin), itemView.top.toFloat(), max(end, begin), itemView.bottom.toFloat()),
                pos
            )
            begin = end
        }
        return false
    }

    private fun getCountButtonOfPosition(
        buffer: MutableList<CustomSwipeButton>,
        position: ButtonPosition
    ): Byte {
        var countButton: Byte = 0
        buffer.forEach {
            if (it.typeButtonPosition == position) {
                countButton++
            }
        }
        return countButton
    }
    private fun getTotalWightButtonOfPosition(
        buffer: MutableList<CustomSwipeButton>,
        position: ButtonPosition
    ): Float {
        var totalWightButton = 0F
        buffer.forEach {
            if (it.typeButtonPosition == position) {
                totalWightButton += it.width
            }
        }
        return totalWightButton
    }

}