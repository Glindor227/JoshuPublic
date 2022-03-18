package com.example.joshu.utils

import android.text.TextUtils
import android.util.Base64
import android.util.Log
import java.security.MessageDigest

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object CipherUtils {
    private val secret =
        byteArrayOf(0x5, 0x50, 0x23, 0x28, 0x3, 0x50, 0x26, 0x68, 0x4, 0x52, 0x33, 0x38, 0x2, 0x56, 0x3, 0x02)

    fun encode(string: String): String {
        if (TextUtils.isEmpty(string)) {
            return ""
        }
        // Set up secret key spec for 128-bit AES encryption and decryption
        var sks: SecretKeySpec? = null
        try {
            sks = SecretKeySpec(secret, "AES")
        } catch (e: Exception) {
            Log.e(CipherUtils::class.java.simpleName, "AES secret key spec error")
        }

        // Encode the original data with AES
        var encodedBytes: ByteArray? = null
        try {
            val c = Cipher.getInstance("AES")
            c.init(Cipher.ENCRYPT_MODE, sks)
            encodedBytes = c.doFinal(string.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(CipherUtils::class.java.simpleName, "AES encryption error")
        }

        return Base64.encodeToString(encodedBytes, Base64.NO_WRAP)
    }

    fun decode(string: String): String {
        if (TextUtils.isEmpty(string)) {
            return ""
        }

        // Set up secret key spec for 128-bit AES encryption and decryption
        var sks: SecretKeySpec? = null
        try {
            sks = SecretKeySpec(secret, "AES")
        } catch (e: Exception) {
            Log.e(CipherUtils::class.java.simpleName, "AES secret key spec error")
        }

        val encodedBytes = Base64.decode(string, Base64.NO_WRAP)
        // Decode the encoded data with AES
        try {
            val c = Cipher.getInstance("AES")
            c.init(Cipher.DECRYPT_MODE, sks)
            val decodedBytes = c.doFinal(encodedBytes)
            return String(decodedBytes)
        } catch (e: Exception) {
            Log.e(CipherUtils::class.java.simpleName, "AES decryption error")
        }
        return ""
    }

    fun encodeSHA256(string: String): String {
        val bytes = string.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}
