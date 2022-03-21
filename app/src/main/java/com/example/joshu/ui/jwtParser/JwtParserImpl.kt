package com.example.joshu.ui.jwtParser

import com.auth0.android.jwt.JWT
import com.example.joshu.mvp.model.api.IJwtData
import com.example.joshu.mvp.model.api.IJwtParser

class JwtParserImpl: IJwtParser {
    override fun getJwtData(jwtToken: String): IJwtData {
        val jwt = JWT(jwtToken)

        val userId = jwt.getClaim(IJwtParser.JWT_USER_ID).asString()
        val expiration = jwt.getClaim(IJwtParser.JWT_EXPIRATION_TIME).asLong()

        return object: IJwtData{
            override val userId: String
                get() = userId ?: ""
            override val expiration: Long
                get() = expiration ?: 0
        }
    }
}