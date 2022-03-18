package com.example.joshu.mvp.model.api.network.error

import java.lang.RuntimeException

class ServerNotAuthException: RuntimeException("user is not auth")