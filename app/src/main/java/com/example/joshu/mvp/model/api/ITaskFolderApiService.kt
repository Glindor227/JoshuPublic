package com.example.joshu.mvp.model.api

import com.example.joshu.mvp.model.api.request.taskFolder.TaskFolderRequest
import com.example.joshu.mvp.model.api.request.taskFolder.TaskFolderUpdateRequest
import com.example.joshu.mvp.model.api.response.taskFolder.TaskFolderAllResponse
import com.example.joshu.mvp.model.api.response.taskFolder.TaskFolderResponse
import retrofit2.http.*

interface ITaskFolderApiService {
    @POST("/api/v1/task_folder/")
    suspend fun taskFolderCreate(@Header("Authorization") token: String,
                                 @Body request: TaskFolderRequest): TaskFolderResponse

    @GET("/api/v1/task_folder/all/?")
    suspend fun taskFolderAll(@Header("Authorization") token: String,
                              @Query("date") date: Int): TaskFolderAllResponse

    @PUT("/api/v1/task_folder/{id}")
    suspend fun taskFolderUpdate(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: TaskFolderUpdateRequest): TaskFolderResponse

    @DELETE("/api/v1/task_folder/{id}")
    suspend fun taskFolderDelete(@Header("Authorization") token: String,
                                 @Path("id") id: Int)
}