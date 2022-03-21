package com.example.joshu.utils

import com.example.joshu.di.Injector
import com.example.joshu.mvp.model.IBufferTaskUtil
import com.example.joshu.mvp.model.ISharedPreferences
import com.example.joshu.ui.mainScreen.today.TaskData
import com.google.gson.Gson
import javax.inject.Inject

class BufferTaskUtilImpl: IBufferTaskUtil {

    @Inject
    lateinit var sharedPreferences: ISharedPreferences

    init {
        Injector.getInstance().appComponent().inject(this)
    }

    private fun serializeToGson(taskData: TaskData): String {
        return Gson().toJson(taskData)
    }

    private fun deserializeFromGson(gson: String?): TaskData? {
        return Gson().fromJson(gson, TaskData::class.java)
    }

    override fun getTaskFromBuffer(): TaskData? {
        val serializedTask = sharedPreferences.gsonTask
        return deserializeFromGson(serializedTask)
    }

    override fun putTaskToBuffer(taskData: TaskData) {
        val serializedTask = serializeToGson(taskData)
        sharedPreferences.gsonTask = serializedTask
    }

    override fun clearTaskBuffer() {
        sharedPreferences.gsonTask = null
    }
}