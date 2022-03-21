package com.example.joshu.mvp.model.api

import com.example.joshu.BuildConfig
import com.example.joshu.di.module.RetrofitModule
import com.example.joshu.mvp.model.ISharedPreferences
import com.example.joshu.mvp.model.api.request.user.TokenRequest
import com.example.joshu.mvp.model.api.response.TokenResponse
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class TokenAuthenticator(private val sharedPreferences: ISharedPreferences,
                         private val jwtParser: IJwtParser): Authenticator {
    companion object {
        const val MAX_TRY_COUNT_PER_TIME = 3
        const val INTERVAL_TRY = 3 * 1000L
    }

    private var tryCount = 0
    private var lastTimeTry = 0L

    override fun authenticate(route: Route?, response: Response): Request? {
        val currentTime = System.currentTimeMillis()
        val interval = currentTime - lastTimeTry

        if (interval > INTERVAL_TRY) {
            tryCount = 0
        }

        lastTimeTry = currentTime

        if (response.code == 401 && tryCount < MAX_TRY_COUNT_PER_TIME) {
            tryCount++

            val refreshResult = refreshToken()
            if (refreshResult) {
                //refresh is successful
                val newAccess = sharedPreferences.accessToken

                // make current request with new access token
                return response.request.newBuilder()
                    .header(RetrofitModule.API_HEADER_KEY, RetrofitModule.TOKEN_PREFIX + newAccess)
                    .build()
            }
        }

        return null
    }

    @Throws(IOException::class)
    fun refreshToken(): Boolean {
        val accessToken = sharedPreferences.accessToken
        val refreshToken = sharedPreferences.refreshToken

        if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
            return false
        }

        val gson = Gson()

        val json = "application/json; charset=utf-8".toMediaTypeOrNull()

        val requestClass = TokenRequest(refreshToken)
        val requestBody = RequestBody.create(json, gson.toJson(requestClass))

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(BuildConfig.API_URL + "api/v1/token/refresh/")
            .method("POST", requestBody)
            .addHeader(RetrofitModule.API_HEADER_KEY, RetrofitModule.TOKEN_PREFIX + accessToken)
            .build()

        try {
            val response = client.newCall(request).execute()
            if (response.code == 200) {
                // Get response
                val jsonData = response.body?.string()

                val refreshTokenResult = gson.fromJson(jsonData, TokenResponse::class.java)

                val jwtData = jwtParser.getJwtData(refreshTokenResult.accessToken)

                sharedPreferences.accessToken = refreshTokenResult.accessToken
                sharedPreferences.refreshToken = refreshTokenResult.refreshToken
                sharedPreferences.expirationToken = jwtData.expiration
                return true
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }
}