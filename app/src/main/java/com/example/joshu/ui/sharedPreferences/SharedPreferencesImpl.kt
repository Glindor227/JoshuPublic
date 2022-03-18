package com.example.joshu.ui.sharedPreferences

import android.content.SharedPreferences
import com.example.joshu.mvp.model.ISharedPreferences
import com.example.joshu.utils.CipherUtils

class SharedPreferencesImpl(private val preferences: SharedPreferences):
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


    override fun setAccessToken(token: String) {
        save(ISharedPreferences.ACCESS_TOKEN, token)
    }

    override fun getAccessToken(): String? {
        return getString(ISharedPreferences.ACCESS_TOKEN)
    }

    override fun resetTokens() {
        save(ISharedPreferences.ACCESS_TOKEN, null)
    }

    override fun setPassword(password: String) {
        save(ISharedPreferences.PASSWORD, encode(password))
    }

    override fun getPassword(): String? {
        val encodePassword = getString(ISharedPreferences.PASSWORD)
        return if (encodePassword != null) {
            decode(encodePassword)
        } else {
            encodePassword
        }
    }

    override fun resetAuth() {
        save(ISharedPreferences.PASSWORD, null)
    }

    override fun setUserId(id: Int) {
        save(ISharedPreferences.USER_ID, id)
    }

    override fun getUserId(): Int {
        return getInt(ISharedPreferences.USER_ID)
    }

    override fun clear() {
        preferences.edit()
            .clear()
            .apply()
    }
}