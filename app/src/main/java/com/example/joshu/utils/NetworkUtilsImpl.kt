package com.example.joshu.utils

import android.content.Context
import android.net.ConnectivityManager
import com.example.joshu.mvp.model.INetworkUtils

class NetworkUtilsImpl(private val context: Context):
    INetworkUtils {

    override fun isInternetOn(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        if (connectivityManager is ConnectivityManager) {
            val activeNetwork = connectivityManager.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnected
        }
        return false
    }
}