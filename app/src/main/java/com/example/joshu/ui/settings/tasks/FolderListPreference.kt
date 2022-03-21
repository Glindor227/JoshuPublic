package com.example.joshu.ui.settings.tasks

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.joshu.R
import com.example.joshu.mvp.model.entity.room.TaskFolder

class FolderListPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs),
    FolderListInteraction {

    companion object {
        const val DEFAULT_ID = 0
    }

    private var adapter: RadioGroupAdapter? = null
    private var entries: List<TaskFolder> = ArrayList()
    private var selectedEntry: Int = DEFAULT_ID

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        val recyclerView: RecyclerView = holder?.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = RadioGroupAdapter(this)
        recyclerView.adapter = adapter
    }

    override fun getItem(position: Int): TaskFolder {
        return entries[position]
    }

    override fun getItemsCount(): Int {
        return entries.size
    }

    override fun getSelectedId(): Int {
        return selectedEntry
    }

    override fun onSelectRadio(position: Int) {
        selectedEntry = entries[position].id
        persistInt(selectedEntry)
        notifyChanged()
        callChangeListener(selectedEntry)
    }

    fun setEntries(newEntries: List<TaskFolder>) {
        entries = newEntries
        if (entries.isNotEmpty() && entries.none { it.id == selectedEntry }) {
            selectedEntry = DEFAULT_ID
            persistInt(selectedEntry)
        }
        adapter?.notifyDataSetChanged()
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        super.onSetInitialValue(defaultValue)
        selectedEntry = if (defaultValue is Int) {
            getPersistedInt(defaultValue)
        } else {
            getPersistedInt(DEFAULT_ID)
        }
        persistInt(selectedEntry)
    }
}
