package com.example.joshu.mvp.model

interface IValidationUtils {
    fun isValidEmail(email: String): Boolean
    fun isValidPassword(password: String): Boolean
}