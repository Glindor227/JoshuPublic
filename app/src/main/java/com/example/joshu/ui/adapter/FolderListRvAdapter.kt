package com.example.joshu.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.example.joshu.R
import com.example.joshu.mvp.presenter.list.IFolderListPresenter
import com.example.joshu.mvp.view.list.IFolderListRowView
import com.example.joshu.utils.ViewsUtil

class FolderListRvAdapter(private val presenter: IFolderListPresenter) :
    RecyclerView.Adapter<FolderListRvAdapter.ViewHolderFolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_item_folder, parent, false
        )
        return ViewHolderFolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolderFolder, position: Int) {
        holder.mPos = position
        holder.reset()
        presenter.bind(holder)
        holder.itemView.setOnClickListener { presenter.onClick(position) }
        holder.itemView.setOnLongClickListener {
            presenter.onLongClick(position)
            true
        }
    }

    override fun getItemCount(): Int {
        return presenter.getCount()
    }

    inner class ViewHolderFolder(view: View) : SimpleRecyclerViewHolder(view), IFolderListRowView {

        @BindView(R.id.folderIconView)
        lateinit var folderIconView: ImageView

        @BindView(R.id.folderTitleView)
        lateinit var titleView: TextView

        @BindView(R.id.countOfTasksView)
        lateinit var countOfTasksView: TextView

        @BindView(R.id.divider)
        lateinit var divider: View


        override fun setEmptyIcon() {
            folderIconView.setImageResource(R.drawable.ic_folder)
        }

        override fun setFilledIcon() {
            folderIconView.setImageResource(R.drawable.ic_folder_selected)
        }

        override fun setTitle(title: String) {
            titleView.text = title
        }

        override fun setCountOfTasks(countOfTasks: Int) {
            countOfTasksView.text = countOfTasks.toString()
        }

        override fun showDivider() = ViewsUtil.showViews(divider)

        override fun hideDivider() = ViewsUtil.goneViews(divider)

        override fun reset() {
            folderIconView.imageTintList = null
            ViewsUtil.clearTextViews(titleView, countOfTasksView)
        }

    }
}