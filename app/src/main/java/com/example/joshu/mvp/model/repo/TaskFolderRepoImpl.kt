package com.example.joshu.mvp.model.repo

import com.example.joshu.mvp.model.ISharedPreferences
import com.example.joshu.mvp.model.api.ITaskFolderApiService
import com.example.joshu.mvp.model.api.network.error.NetworkParserError
import com.example.joshu.mvp.model.api.request.taskFolder.mapToTaskFolderRequest
import com.example.joshu.mvp.model.api.network.Result
import com.example.joshu.mvp.model.api.network.error.ServerNotAuthException
import com.example.joshu.mvp.model.api.request.taskFolder.mapToTaskFolderUpdateRequest
import com.example.joshu.mvp.model.api.request.taskFolder.millisToSeconds
import com.example.joshu.mvp.model.api.response.taskFolder.toTaskFolder
import com.example.joshu.mvp.model.entity.room.TaskFolder

class TaskFolderRepoImpl(private val apiService: ITaskFolderApiService,
                         private val settings: ISharedPreferences,
                         networkParserError: NetworkParserError)
    : BaseRepo(networkParserError), ITaskFolderRepo {

    override suspend fun sendTaskFolder(taskFolder: TaskFolder): Result<TaskFolder> {
        val accessToken = "Bearer ${settings.accessToken}"
        val request = taskFolder.mapToTaskFolderRequest()
        val response = safeApiCall(call = {
            apiService.taskFolderCreate(accessToken, request)
        })

        return when(response) {
            is Result.Success -> {
                Result.Success(response.data.toTaskFolder())
            }
            is Result.Error -> Result.Error(response.exception)
        }
    }

    override suspend fun updateTaskFolder(taskFolder: TaskFolder): Result<Unit> {
        val accessToken = "Bearer ${settings.accessToken}"
        val request = taskFolder.mapToTaskFolderUpdateRequest()
        val response = safeApiCall(call = {
            apiService.taskFolderUpdate(accessToken, taskFolder.id, request)
        })
        return when (response) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> response
        }
    }

    override suspend fun getAllTaskFolders(date: Long): Result<List<TaskFolder>> {
        val accessToken = "Bearer ${settings.accessToken}"
        val response = safeApiCall(call = {
            apiService.taskFolderAll(accessToken, date.millisToSeconds().toInt())
        })

        return when (response) {
            is Result.Success -> {
                Result.Success(response.data.data?.map { it.toTaskFolder() } ?: listOf())
            }
            is Result.Error -> response
        }
    }

    override suspend fun deleteTaskFolder(id: Int): Result<Unit> {
        val accessToken = "Bearer ${settings.accessToken}"
        val response = safeApiCall(call = { apiService.taskFolderDelete(accessToken, id) })

        return when (response) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> response
        }
    }
}