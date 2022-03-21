package com.example.joshu.mvp.model.api

import com.example.joshu.mvp.model.api.request.task.TaskCreateRequest
import com.example.joshu.mvp.model.api.request.task.TaskUpdateRequest
import com.example.joshu.mvp.model.api.response.task.AllTasksResponse
import com.example.joshu.mvp.model.api.response.task.TaskResponse
import retrofit2.http.*

interface ITaskApiService {

    @POST("api/v1/task/")
    suspend fun taskCreate(@Header("Authorization") token: String,
                           @Body request: TaskCreateRequest): TaskResponse

    @GET("api/v1/task/all/?")
    suspend fun taskGetAll(@Header("Authorization") token: String,
                           @Query("date") date: Long): AllTasksResponse

    @PUT("api/v1/task/{id}/")
    suspend fun putTask(@Header("Authorization") token: String,
                        @Path("id") id: Int,
                        @Body request: TaskUpdateRequest): TaskResponse

    @DELETE("api/v1/task/{id}/")
    suspend fun deleteTask(@Header("Authorization") token: String,
                           @Path("id") id: Int)

    @POST("api/v1/transfer_task/{task_id}/")
    suspend fun transferTask(@Header("Authorization") token: String,
                             @Path("id") id: Int)
}