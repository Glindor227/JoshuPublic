package com.example.joshu.utils

import com.example.joshu.BuildConfig
import timber.log.Timber

object LoggerHelper {
    fun error(obj: Any, msg: String, throwable: Throwable?, sendToCrashlystic: Boolean) {
        if (throwable != null) {
            Timber.e(throwable, msg)
        } else {
            Timber.e(msg)
        }

        if (!BuildConfig.DEBUG && sendToCrashlystic) {
//            if (throwable != null) {
//                Crashlytics.logException(throwable)
//            }
//            if (!TextUtils.isEmpty(msg)) {
//                Crashlytics.log(msg)
//            }
        }
    }

    fun debug(obj: Any, msg: String) {
        if (BuildConfig.DEBUG || BuildConfig.ENABLE_LOG) {
            Timber.e(msg)
        }
    }
}
