package com.example.joshu.mvp.model


interface ISharedPreferences {
    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val EXPIRATION = "EXPIRATION"
        const val PASSWORD = "PASSWORD"
        const val USER_ID = "USER_ID"
        const val USER_DISPLAY_NAME = "USER_DISPLAY_NAME"
        const val GSON_TEMP_TASK = "TEMP_TASK"
        const val WIDGET_SIZE = "WIDGET_SIZE"
        const val DEFAULT_LIST = "DEFAULT_LIST"
        const val USER_EMAIL = "USER_EMAIL"
        const val USER_PHONE = "USER_PHONE"
        const val LIFEHACK_VISIBILITY = "LIFEHACK_VISIBILITY"
        const val CALENDAR_VISIBILITY = "CALENDAR_VISIBILITY"
        const val FOLDERS_VISIBILITY = "FOLDERS_VISIBILITY"
        const val DOING_VISIBILITY = "DOING_VISIBILITY"
        const val WAITING_VISIBILITY = "WAITING_VISIBILITY"
        const val READY_TASKS_VISIBILITY = "READY_TASKS_VISIBILITY"
        const val SMART_NOTIFICATIONS = "SMART_NOTIFICATIONS"
        const val VIBRATION = "VIBRATION"
        const val NOTIFICATION_SOUND = "NOTIFICATION_SOUND"
        const val NOTIFICATION_BOT = "NOTIFICATION_BOT"
        const val NOTIFICATION_PUSH = "NOTIFICATION_PUSH"
        const val TIME_FORMAT = "TIME_FORMAT"
        const val START_OF_WEEK = "START_OF_WEEK"
    }

    var accessToken: String?
    var refreshToken: String?
    var expirationToken: Long
    fun resetTokens()

    var password: String?
    fun resetAuth()

    var userId: String?
    var userDisplayName: String?
    var gsonTask: String?
    fun encode(what: String): String
    fun clear()

    var isSmallWidget: Boolean

    var defaultFolderId: Int

    var userEmail: String?
    var userPhone: String?

    var isLifehackBarVisible: Boolean
    var isCalendarBarVisible: Boolean
    var isFoldersBarVisible: Boolean
    var isDoingBarVisible: Boolean
    var isWaitingBarVisible: Boolean

    var smartNotificationsIsOn: Boolean
    var vibrationIsOn: Boolean
    var notificationSoundIsOn: Boolean
    var chatbotNotificationIsOn: Boolean
    var pushNotificationsIsOn: Boolean

    var showReadyTasksIsOn: Boolean

    var firstDayOfWeek: Int
    var is24HourTimeFormat: Boolean


}