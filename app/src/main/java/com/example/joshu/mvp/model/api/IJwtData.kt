package com.example.joshu.mvp.model.api

interface IJwtData {
    val userId: String
    val expiration: Long
}