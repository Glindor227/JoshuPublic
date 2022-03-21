package com.example.joshu.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.example.joshu.R
import com.example.joshu.mvp.model.entity.PriorityTypeEnum
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.mvp.presenter.list.ITaskListPresenter
import com.example.joshu.mvp.view.list.ITaskListRowView
import com.example.joshu.utils.ViewsUtil
import com.example.joshu.viewUtils.PriorityUiUtils
import com.google.android.material.chip.Chip
import com.ms.square.android.expandabletextview.ExpandableTextView

class TaskListRvAdapter(private val presenter: ITaskListPresenter) :
    RecyclerView.Adapter<TaskListRvAdapter.ViewHolderTask>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTask {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_item_task_card, parent, false
        )
        return ViewHolderTask(view)
    }

    override fun getItemCount(): Int = presenter.getCount()

    override fun onBindViewHolder(holder: ViewHolderTask, position: Int) {
        holder.mPos = position
        holder.reset()

        presenter.bind(holder)

        holder.itemView.setOnClickListener {
            presenter.onClick(position)
        }
    }

    inner class ViewHolderTask(view: View) : SimpleRecyclerViewHolder(view), ITaskListRowView {
        @BindView(R.id.priorityView)
        lateinit var priorityView: ImageView

        @BindView(R.id.expand_text_view)
        lateinit var titleView: ExpandableTextView

        @BindView(R.id.folderView)
        lateinit var folderView: Chip

        @BindView(R.id.timeView)
        lateinit var timeView: TextView

        var statusTask: Int? = null

        override fun reset() {
            priorityView.imageTintList = null
            ViewsUtil.clearTextViews(folderView, timeView)
            titleView.text = ""
        }

        override fun setPriority(priorityTypeEnum: PriorityTypeEnum) {
            val resource = PriorityUiUtils.getResourceImage(priorityTypeEnum)
            priorityView.setImageResource(resource)
        }

        override fun setTitle(title: String) {
            titleView.text = title
        }

        override fun setTaskFolder(taskFolder: TaskFolder) {
            folderView.text = taskFolder.title
            val color = Color.parseColor(taskFolder.color)
            folderView.chipStrokeColor = ColorStateList.valueOf(color)
        }

        override fun setDateTime(dateTime: String) {
            timeView.text = dateTime
        }

        override fun setStatus(status: Int) {
            statusTask = status
        }

        override fun setFolderVisibility(isVisible: Boolean) {
            if (isVisible) {
                ViewsUtil.showViews(folderView)
            } else {
                ViewsUtil.hideViews(folderView)
            }
        }
    }
}