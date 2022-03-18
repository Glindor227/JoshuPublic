package com.example.joshu.mvp.model.api.network.error

data class NetworkError(val code: Int,
                        val message: String) {
    companion object {
        const val USER_IS_NOT_AUTH = 1
    }
}