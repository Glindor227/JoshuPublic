package com.example.joshu.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.example.joshu.mvp.view.list.IBaseListRowView

abstract class SimpleRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
    IBaseListRowView {
    var mPos: Int = 0

    companion object {
        fun createView(@LayoutRes resId: Int, parent: ViewGroup): View {
            val inflater = LayoutInflater.from(parent.context)
            return inflater.inflate(resId, parent, false)
        }
    }

    init {
        ButterKnife.bind(this, itemView)
    }

    override fun getPos(): Int {
        return mPos
    }
}