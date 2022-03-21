package com.example.joshu.mvp.model.repo

import android.util.Log
import com.example.joshu.mvp.model.ISharedPreferences
import com.example.joshu.mvp.model.api.IApiService
import com.example.joshu.mvp.model.api.IJwtParser
import com.example.joshu.mvp.model.api.network.Result
import com.example.joshu.mvp.model.api.network.error.NetworkParserError
import com.example.joshu.mvp.model.api.request.user.AuthUserRequest
import com.example.joshu.mvp.model.api.request.user.TimeZoneRequest
import com.example.joshu.mvp.model.api.request.user.TokenRequest
import com.example.joshu.mvp.model.api.response.TokenResponse
import com.example.joshu.mvp.model.api.response.user.TimeZoneResponse
import com.example.joshu.mvp.model.exception.RefreshTokenIsNull
import java.util.*

class UserRepo(private val apiService: IApiService,
               private val jwtParser: IJwtParser,
               private val settings: ISharedPreferences,
               networkParserError: NetworkParserError)
    : BaseRepo(networkParserError) {

    private fun updateToken(response: TokenResponse) {
        val jwtData = jwtParser.getJwtData(response.accessToken)

        settings.accessToken = response.accessToken
        settings.refreshToken = response.refreshToken
        settings.expirationToken = jwtData.expiration
    }

    suspend fun authUserOnServer(userId: String, displayName: String?): Result<Unit> {
        val request = AuthUserRequest(userId, displayName)
        val response = safeApiCall (call = { apiService.authUser(request) } )

        return when(response) {
            is Result.Success -> {
                updateToken(response.data)
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(response.exception)
        }
    }

    suspend fun telegramLinkUser(): Result<Unit> {
        val response = safeApiCall(call = { apiService.telegramLinkUser(settings.accessToken!!) })

        return when (response) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(response.exception)
        }
    }

    suspend fun logout(): Result<Unit> {
        val accessToken = "Bearer ${settings.accessToken}"
        val response = safeApiCall (call = { apiService.logoutUser(accessToken) })
        return when (response) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> response
        }
    }

    suspend fun setTimeZone(): Result<TimeZoneResponse> {
        val timeZoneID = TimeZone.getDefault().id
        val request = TimeZoneRequest(timeZoneID)
        val accessToken = "Bearer ${settings.accessToken}"
        val response = safeApiCall (call = { apiService.setTimeZone(accessToken, request) })

        return when (response) {
            is Result.Success -> response
            is Result.Error -> response
        }
    }

    suspend fun refreshToken(): Result<Unit> {
        val refreshToken = settings.refreshToken ?: return Result.Error(RefreshTokenIsNull())
        val request = TokenRequest(refreshToken)
        val accessToken = "Bearer ${settings.accessToken}"
        val response = safeApiCall (call = { apiService.refreshToken(accessToken, request) })
        when(response) {
            is Result.Success -> {
                updateToken(response.data)
                return Result.Success(Unit)
            }

            is Result.Error -> return Result.Error(response.exception)
        }
    }
}