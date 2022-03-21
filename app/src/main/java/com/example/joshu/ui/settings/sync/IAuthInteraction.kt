package com.example.joshu.ui.settings.sync

interface IAuthInteraction {

    fun loginWithGoogle()
    fun loginWithFacebook()
    fun loginWithVK()
    fun loginWithEmail()
    fun forgotPassword()
}