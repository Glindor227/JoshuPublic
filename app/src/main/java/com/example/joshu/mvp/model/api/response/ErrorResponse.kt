package com.example.joshu.mvp.model.api.response

import com.example.joshu.mvp.model.api.network.error.NetworkError

data class ErrorResponse(val errors: List<NetworkError>) {
    fun isEmpty(): Boolean {
        return errors.isEmpty()
    }
}