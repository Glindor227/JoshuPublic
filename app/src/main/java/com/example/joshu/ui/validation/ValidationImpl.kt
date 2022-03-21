package com.example.joshu.ui.validation

import com.example.joshu.mvp.model.IValidationUtils

class ValidationImpl: IValidationUtils {
    override fun isValidEmail(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    override fun isValidPassword(password: String): Boolean =
        password.isNotEmpty()
}