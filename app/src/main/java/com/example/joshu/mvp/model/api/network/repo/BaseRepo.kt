package com.example.joshu.mvp.model.api.network.repo

import com.example.joshu.mvp.model.api.network.error.NetworkParserError
import com.example.joshu.mvp.model.api.network.Result

abstract class BaseRepo(private val networkParserError: NetworkParserError) {
    suspend fun <T: Any> safeApiCall(call: suspend () -> T): Result<T> {

        val result: Result<T> = safeApiResult(call)

        return when(result) {
            is Result.Success ->
                result
            is Result.Error -> {
                Result.Error(networkParserError.transformExceptionHttp(result.exception))
            }
        }
    }

    private suspend fun <T: Any> safeApiResult(call: suspend () -> T): Result<T> {
        return try {
            Result.Success(call.invoke())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}