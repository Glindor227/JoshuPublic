package com.example.joshu.viewUtils

import android.graphics.Color
import kotlin.random.Random

object ColorUtils {

    fun getRandomBrightColor(): String {
        val hue = getRandomHue()
        val saturation = getRandomSaturation()
        val value = getRandomBrightness()
        val hsv = floatArrayOf(hue, saturation, value)
        val color = Color.HSVToColor(0, hsv)
        return color.toHexColor()
    }

    private fun getRandomHue(): Float = Random.nextInt(0, 361).toFloat()

    private fun getRandomSaturation(): Float = Random.nextInt(30, 101) / 100.toFloat()

    private fun getRandomBrightness(): Float = Random.nextInt(60, 101) / 100.toFloat()

    private fun Int.toHexColor(): String = "#${this.toString(16).takeLast(6).padEnd(6, '0')}"

}