package com.example.joshu.mvp.model.api.network.error

import java.lang.RuntimeException

class ServerException(val errors: List<NetworkError>): RuntimeException()