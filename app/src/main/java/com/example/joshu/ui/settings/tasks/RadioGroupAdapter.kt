package com.example.joshu.ui.settings.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.example.joshu.R
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.ui.adapter.SimpleRecyclerViewHolder

class RadioGroupAdapter(
    private val interaction: FolderListInteraction
) : RecyclerView.Adapter<RadioGroupAdapter.RadioGroupHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioGroupHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.radio_button_item, parent, false)
        return RadioGroupHolder(view)
    }

    override fun onBindViewHolder(holder: RadioGroupHolder, position: Int) {
        holder.bind(interaction.getItem(position), interaction.getSelectedId())
        holder.btn.setOnClickListener { interaction.onSelectRadio(position) }
    }

    override fun getItemCount(): Int {
        return interaction.getItemsCount()
    }

    inner class RadioGroupHolder(itemView: View) : SimpleRecyclerViewHolder(itemView) {

        @BindView(R.id.radioBtn)
        lateinit var btn: RadioButton

        fun bind(folder: TaskFolder, selectedId: Int) {
            btn.text = folder.title
            btn.isChecked = folder.id == selectedId
        }

        override fun reset() {
            btn.text = ""
            btn.isChecked = false
        }
    }
}