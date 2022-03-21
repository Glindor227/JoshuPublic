package com.example.joshu.ui.settings.premium

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.example.joshu.R
import com.example.joshu.ui.adapter.SimpleRecyclerViewHolder

class ViewPagerAdapter(private val items: List<AdvertContent>) :
    RecyclerView.Adapter<ViewPagerAdapter.AdvertHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.dialog_premium_item, parent, false)
        return AdvertHolder(view)
    }

    override fun onBindViewHolder(holder: AdvertHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class AdvertHolder(itemView: View) : SimpleRecyclerViewHolder(itemView) {

        @BindView(R.id.advertImage)
        lateinit var imageView: ImageView

        @BindView(R.id.advertText)
        lateinit var textView: TextView

        fun bind(advertContent: AdvertContent) {
            imageView.setImageResource(advertContent.imageId)
            textView.text = advertContent.text
        }

        override fun reset() {
            imageView.imageTintList = null
            textView.text = ""
        }

    }

}