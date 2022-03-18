package com.example.joshu.ui.strings

import android.content.Context
import com.example.joshu.R
import com.example.joshu.mvp.model.IStrings

class AndroidStringsByResourcesImpl(private val context: Context): IStrings {
    override fun internetError(): String {
        return context.getString(R.string.alert_internet_problem)
    }

    override fun serverError(): String {
        return context.getString(R.string.unknown_server_error)
    }

    override fun unknownError(): String {
        return context.getString(R.string.unknown_error)
    }
}