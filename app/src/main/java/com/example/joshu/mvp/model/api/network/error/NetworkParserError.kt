package com.example.joshu.mvp.model.api.network.error

import com.example.joshu.mvp.model.api.response.ErrorResponse
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException
import java.lang.Exception

class NetworkParserError(private val retrofit: Retrofit) {
    fun transformExceptionHttp(cause: Throwable): Throwable {
        if (cause is HttpException) {
            val statusCode = cause.code()
            val responseBody = cause.response()?.errorBody()

            responseBody?.let {
                return when (statusCode) {
                    401 -> ServerNotAuthException()
                    400, 403, 404, 409, 422, 500, 503 -> {
                        getServerException(responseBody)
                    }
                    else -> cause
                }
            }
        }
        return cause
    }

    private fun getServerException(responseBody: ResponseBody): ServerException {
        val errorResponse = getErrorBody(responseBody)

        return if (errorResponse != null) {
            try {
                ServerException(errorResponse.errors)
            } catch (e: Exception) {
                ServerException(emptyList())
            }
        } else {
            ServerException(emptyList())
        }
    }

    private fun getErrorBody(responseBody: ResponseBody): ErrorResponse? {
        try {
            val errorConverter = retrofit.responseBodyConverter<ErrorResponse>(ErrorResponse::class.java, arrayOfNulls<Annotation>(0))
            return try {
                errorConverter.convert(responseBody)
            } catch (exception: IOException) {
                null
            }
        } catch (exception: Exception) {
            return null
        }
    }
}