package com.example.joshu.ui.sharedPreferences

import android.content.SharedPreferences
import com.example.joshu.mvp.model.ISharedPreferences
import com.example.joshu.utils.CipherUtils

class SharedPreferencesImpl(private val preferences: SharedPreferences) :
    ISharedPreferences {
    /** private **/

    override fun encode(what: String): String {
        return CipherUtils.encode(what)
    }

    private fun decode(what: String): String {
        return CipherUtils.decode(what)
    }

    private fun save(key: String, value: String?) {
        preferences.edit()
            .putString(key, value)
            .apply()
    }

    private fun save(key: String, value: Boolean) {
        preferences.edit()
            .putBoolean(key, value)
            .apply()
    }

    private fun save(key: String, value: Int) {
        preferences.edit()
            .putInt(key, value)
            .apply()
    }

    private fun save(key: String, value: Long) {
        preferences.edit()
            .putLong(key, value)
            .apply()
    }

    private fun save(key: String, value: Float) {
        preferences.edit()
            .putFloat(key, value)
            .apply()
    }

    private fun getString(key: String): String? {
        return getString(key, null)
    }

    private fun getString(key: String, defaultValue: String?): String? {
        return preferences.getString(key, defaultValue)
    }

    private fun getBool(key: String): Boolean {
        return getBool(key, false)
    }

    private fun getBool(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    private fun getInt(key: String): Int {
        return getInt(key, 0)
    }

    private fun getInt(key: String, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    private fun getLong(key: String): Long {
        return getLong(key, 0)
    }

    private fun getLong(key: String, defaultValue: Long): Long {
        return preferences.getLong(key, defaultValue)
    }

    private fun getFloat(key: String): Float {
        return getFloat(key, 0f)
    }

    private fun getFloat(key: String, defaultValue: Float): Float {
        return preferences.getFloat(key, defaultValue)
    }


    /** override **/

    override var accessToken: String?
        get() = getString(ISharedPreferences.ACCESS_TOKEN)
        set(value) {
            save(ISharedPreferences.ACCESS_TOKEN, value)
        }
    override var refreshToken: String?
        get() = getString(ISharedPreferences.REFRESH_TOKEN)
        set(value) {
            save(ISharedPreferences.REFRESH_TOKEN, value)
        }
    override var expirationToken: Long
        get() = getLong(ISharedPreferences.EXPIRATION)
        set(value) {
            save(ISharedPreferences.EXPIRATION, value)
        }

    override fun resetTokens() {
        save(ISharedPreferences.ACCESS_TOKEN, null)
    }

    override var password: String?
        get() = getString(ISharedPreferences.PASSWORD)
        set(value) {
            save(ISharedPreferences.PASSWORD, value?.let {
                encode(it)
            })
        }

    override fun resetAuth() {
        save(ISharedPreferences.PASSWORD, null)
    }

    override var userId: String?
        get() = getString(ISharedPreferences.USER_ID)
        set(value) {
            save(ISharedPreferences.USER_ID, value)
        }
    override var userDisplayName: String?
        get() = getString(ISharedPreferences.USER_DISPLAY_NAME)
        set(value) {
            save(ISharedPreferences.USER_DISPLAY_NAME, value)
        }

    override var gsonTask: String?
        get() = getString(ISharedPreferences.GSON_TEMP_TASK)
        set(value) {
            save(ISharedPreferences.GSON_TEMP_TASK, value)
        }

    override fun clear() {
        preferences.edit()
            .clear()
            .apply()
    }

    override var isSmallWidget: Boolean
        get() = getBool(ISharedPreferences.WIDGET_SIZE)
        set(value) {
            save(ISharedPreferences.WIDGET_SIZE, value)
        }

    override var defaultFolderId: Int
        get() = getInt(ISharedPreferences.DEFAULT_LIST)
        set(value) {
            save(ISharedPreferences.DEFAULT_LIST, value)
        }
    override var userEmail: String?
        get() = getString(ISharedPreferences.USER_EMAIL)
        set(value) {
            save(ISharedPreferences.USER_EMAIL, value)
        }
    override var userPhone: String?
        get() = getString(ISharedPreferences.USER_PHONE)
        set(value) {
            save(ISharedPreferences.USER_PHONE, value)
        }
    override var isLifehackBarVisible: Boolean
        get() = getBool(ISharedPreferences.LIFEHACK_VISIBILITY)
        set(value) {
            save(ISharedPreferences.LIFEHACK_VISIBILITY, value)
        }
    override var isCalendarBarVisible: Boolean
        get() = getBool(ISharedPreferences.CALENDAR_VISIBILITY)
        set(value) {
            save(ISharedPreferences.CALENDAR_VISIBILITY, value)
        }
    override var isFoldersBarVisible: Boolean
        get() = getBool(ISharedPreferences.FOLDERS_VISIBILITY)
        set(value) {
            save(ISharedPreferences.FOLDERS_VISIBILITY, value)
        }
    override var isDoingBarVisible: Boolean
        get() = getBool(ISharedPreferences.DOING_VISIBILITY)
        set(value) {
            save(ISharedPreferences.DOING_VISIBILITY, value)
        }
    override var isWaitingBarVisible: Boolean
        get() = getBool(ISharedPreferences.WAITING_VISIBILITY)
        set(value) {
            save(ISharedPreferences.WAITING_VISIBILITY, value)
        }
    override var smartNotificationsIsOn: Boolean
        get() = getBool(ISharedPreferences.SMART_NOTIFICATIONS)
        set(value) {
            save(ISharedPreferences.SMART_NOTIFICATIONS, value)
        }
    override var vibrationIsOn: Boolean
        get() = getBool(ISharedPreferences.VIBRATION)
        set(value) {
            save(ISharedPreferences.VIBRATION, value)
        }
    override var notificationSoundIsOn: Boolean
        get() = getBool(ISharedPreferences.NOTIFICATION_SOUND)
        set(value) {
            save(ISharedPreferences.NOTIFICATION_SOUND, value)
        }
    override var chatbotNotificationIsOn: Boolean
        get() = getBool(ISharedPreferences.NOTIFICATION_BOT)
        set(value) {
            save(ISharedPreferences.NOTIFICATION_BOT, value)
        }
    override var pushNotificationsIsOn: Boolean
        get() = getBool(ISharedPreferences.NOTIFICATION_PUSH)
        set(value) {
            save(ISharedPreferences.NOTIFICATION_PUSH, value)
        }
    override var showReadyTasksIsOn: Boolean
        get() = getBool(ISharedPreferences.READY_TASKS_VISIBILITY)
        set(value) {
            save(ISharedPreferences.READY_TASKS_VISIBILITY, value)
        }
    override var firstDayOfWeek: Int
        get() = getInt(ISharedPreferences.START_OF_WEEK)
        set(value) {
            save(ISharedPreferences.START_OF_WEEK, value)
        }
    override var is24HourTimeFormat: Boolean
        get() = getBool(ISharedPreferences.TIME_FORMAT)
        set(value) {
            save(ISharedPreferences.TIME_FORMAT, value)
        }
}