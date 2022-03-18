package com.example.joshu.mvp.model

interface IStrings {
    val internetError: String
    val serverError: String
    val unknownError: String
    val invalidateEmail: String
    val invalidatePassword: String
    val notMatchPassword: String
    val authUserInfoError: String
    val recoverySuccess: String
    val cancelFacebookSignIn: String
    val invalidatePriority: String
    val invalidateTaskFolder: String
    val invalidateDateTime: String
    val invalidateTaskText: String
    val taskCreateSuccess: String
    val taskTextSaveSuccess: String
    val folderCreateSuccess: String
    val folderDeleteDialogMessage: String
    val dialogPositive: String
    val dialogNegative: String
    val taskUpdateSuccess: String
    val taskSaveChanges: String
    fun nowPrefix(text: String): String
    val errorGetSpeechText: String
    val settingLastUsedList: String
    val settingDefaultListId: Int
    val deleteAccountMessage: String
    val logoutMessage: String
}