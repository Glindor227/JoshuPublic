package com.example.joshu.ui.dialog.folderSelected

import com.example.joshu.mvp.model.entity.room.TaskFolder

interface ISelectedFolderInteraction {
    fun selectedFolder(folder: TaskFolder)
}