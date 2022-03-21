package com.example.joshu.mvp.model.api

interface IJwtParser {
    companion object {
        const val JWT_EXPIRATION_TIME = "exp"
        const val JWT_USER_ID = "user_id"
    }
    fun getJwtData(jwtToken: String): IJwtData
}