package com.example.joshu.ui.settings.tasks

import com.example.joshu.mvp.model.entity.room.TaskFolder

interface FolderListInteraction {
    fun getItem(position: Int): TaskFolder
    fun getItemsCount(): Int
    fun getSelectedId(): Int
    fun onSelectRadio(position: Int)
}