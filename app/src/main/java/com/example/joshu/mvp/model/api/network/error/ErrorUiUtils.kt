package com.example.joshu.mvp.model.api.network.error

import com.example.joshu.mvp.model.IStrings
import retrofit2.HttpException
import java.net.UnknownHostException

object ErrorUiUtils {
    fun getMessageListByThrowable(throwable: Throwable, strings: IStrings): Collection<String> {
        return when (throwable) {
            is ServerException -> {
                if (throwable.errors.isEmpty()) {
                    return listOf(strings.serverError())
                }
                throwable.errors.map {
                    when(it.code) {
                        else -> strings.serverError()
                    }
                }
            }
            is HttpException -> {
                return listOf(strings.serverError())
            }
            is UnknownHostException -> {
                return listOf(strings.internetError())
            }
            else -> {
                emptyList()
            }
        }
    }
}