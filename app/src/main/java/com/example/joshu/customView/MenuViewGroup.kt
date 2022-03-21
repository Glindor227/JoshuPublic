package com.example.joshu.customView

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.marginStart
import com.example.joshu.R
import com.google.android.flexbox.FlexboxLayout
import timber.log.Timber

class MenuViewGroup(context: Context, attrs: AttributeSet): FlexboxLayout(context, attrs) {
    private val listOfItems = arrayListOf<FilterMenuItem>()
    private var singleSelection = false

    fun setSingleSelection(param: Boolean) {
        singleSelection = param
    }

    fun add(item: FilterMenuItem) {
        val listener = object : FilterMenuItem.OnClickCallback{
            override fun onClickCallback(view: View) {
                (view as? FilterMenuItem)?.let {item ->
                    val temp = item.isChecked
                    if (singleSelection) {
                            listOfItems.forEach {
                                if (it != item) it.changeCheckedStatus(false)
                            }
                            item.changeCheckedStatus(!temp)
                    } else {
                        item.isChecked = !temp
                    }
                }
            }
        }
        item.setOnClickCallback(listener)
        listOfItems.add(item)
        super.addView(item)
    }
}