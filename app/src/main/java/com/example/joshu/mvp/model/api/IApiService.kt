package com.example.joshu.mvp.model.api

import com.example.joshu.mvp.model.api.request.user.AuthUserRequest
import com.example.joshu.mvp.model.api.request.user.TimeZoneRequest
import com.example.joshu.mvp.model.api.request.user.TokenRequest
import com.example.joshu.mvp.model.api.response.TokenResponse
import com.example.joshu.mvp.model.api.response.user.TimeZoneResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface IApiService {
    /** auth **/
    @POST("api/v1/user/auth/")
    suspend fun authUser(@Body request: AuthUserRequest): TokenResponse

    @POST("api/v1/user/clear_chat_id/")
    suspend fun clearChatIdUser(@Header("Authorization") token: String)

    @POST("api/v1/user/teleg_link/")
    suspend fun telegramLinkUser(@Header("Authorization") token: String)

    @POST("api/v1/user/time-zone/")
    suspend fun setTimeZone(@Header("Authorization") token: String, @Body request: TimeZoneRequest): TimeZoneResponse

    @POST("api/v1/user/logout/")
    suspend fun logoutUser(@Header("Authorization") token: String)

    @POST("/api/v1/token/refresh/")
    suspend fun refreshToken(@Header("Authorization") token: String, @Body request: TokenRequest): TokenResponse
}