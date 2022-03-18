package com.example.joshu.mvp.model


interface ISharedPreferences {
    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val PASSWORD = "PASSWORD"
        const val USER_ID = "USER_ID"
    }

    fun setAccessToken(token: String)
    fun getAccessToken(): String?
    fun resetTokens()

    fun setPassword(password: String)
    fun getPassword(): String?
    fun resetAuth()

    fun setUserId(id: Int)
    fun getUserId(): Int

    fun encode(what: String): String
    fun clear()
}