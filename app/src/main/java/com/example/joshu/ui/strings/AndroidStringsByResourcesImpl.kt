package com.example.joshu.ui.strings

import android.content.Context
import com.example.joshu.R
import com.example.joshu.mvp.model.IStrings

class AndroidStringsByResourcesImpl(private val context: Context) : IStrings {
    override val internetError: String
        get() = context.getString(R.string.alert_internet_problem)
    override val serverError: String
        get() = context.getString(R.string.unknown_server_error)
    override val unknownError: String
        get() = context.getString(R.string.unknown_error)
    override val invalidateEmail: String
        get() = context.getString(R.string.error_invalidate_email)
    override val invalidatePassword: String
        get() = context.getString(R.string.error_invalidate_password)
    override val notMatchPassword: String
        get() = context.getString(R.string.error_not_match_password)
    override val authUserInfoError: String
        get() = context.getString(R.string.error_auth_user_info)
    override val recoverySuccess: String
        get() = context.getString(R.string.recovery_password_success)
    override val cancelFacebookSignIn: String
        get() = context.getString(R.string.auth_main_cancel_facebook)
    override val invalidatePriority: String
        get() = context.getString(R.string.error_priority)
    override val invalidateTaskFolder: String
        get() = context.getString(R.string.error_task_folder)
    override val invalidateDateTime: String
        get() = context.getString(R.string.error_date_time)
    override val invalidateTaskText: String
        get() = context.getString(R.string.error_task_text)
    override val taskCreateSuccess: String
        get() = context.getString(R.string.task_create_success)
    override val taskUpdateSuccess: String
        get() = context.getString(R.string.task_update_success)
    override val taskSaveChanges: String
        get() = context.getString(R.string.task_save_changes)
    override val taskTextSaveSuccess: String
        get() = context.getString(R.string.task_text_add_to_clipboard_success)
    override val folderCreateSuccess: String
        get() = context.getString(R.string.folder_create_success)
    override val folderDeleteDialogMessage: String
        get() = context.getString(R.string.folder_delete_message)
    override val dialogPositive: String
        get() = context.getString(R.string.dialog_positive_button)
    override val dialogNegative: String
        get() = context.getString(R.string.dialog_negative_button)

    override fun nowPrefix(text: String): String =
        context.getString(R.string.statistic_screen_today_prefix, text)

    override val errorGetSpeechText: String
        get() = context.getString(R.string.error_get_speech_text)
    override val settingLastUsedList: String
        get() = context.getString(R.string.settings_last_used_list)
    override val settingDefaultListId: Int
        get() = context.getString(R.string.settings_default_list_default).toInt()
    override val deleteAccountMessage: String
        get() = context.getString(R.string.settings_delete_message)
    override val logoutMessage: String
        get() = context.getString(R.string.settings_logout_message)
}