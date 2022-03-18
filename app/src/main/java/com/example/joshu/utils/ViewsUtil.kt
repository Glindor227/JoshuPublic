package com.example.joshu.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

object ViewsUtil {
    fun clearImageView(vararg imageViews: ImageView?) {
        clearColorFilterImageView(*imageViews)
        for (iv in imageViews)
            iv?.setImageResource(android.R.color.transparent)
    }

    fun clearColorFilterImageView(vararg imageViews: ImageView?) {
        for (iv in imageViews)
            iv?.colorFilter = null
    }

    fun clearTextViews(vararg textViews: TextView?) {
        for (tv in textViews) {
            tv?.text = ""
        }
    }

    fun goneViews(vararg views: View?) {
        for (v in views) {
            if (v?.visibility != View.GONE) {
                v?.visibility = View.GONE
            }
        }
    }

    fun hideViews(vararg views: View?) {
        for (v in views) {
            if (v?.visibility != View.INVISIBLE) {
                v?.visibility = View.INVISIBLE
            }
        }
    }

    fun showViews(vararg views: View?) {
        for (v in views) {
            if (v?.visibility != View.VISIBLE) {
                v?.visibility = View.VISIBLE
            }
        }
    }

    fun hideSoftKeyboard(view: View?) {
        view?.let {
            val inputMethodManager = it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun showSoftKeyboard(view: View?) {
        view?.let {
            val inputMethodManager = it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun setHideKeyboardOnScrollRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setOnTouchListener { v, _ ->
            hideSoftKeyboard(v)
            false
        }
    }


    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun dpToPx(dp: Float): Float {
        return dp * Resources.getSystem().displayMetrics.density
    }

    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    fun pxToDp(px: Float): Float {
        return px / Resources.getSystem().displayMetrics.density
    }

    fun screenWidthPx(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun isLandscape(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    fun isPortrait(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }
}