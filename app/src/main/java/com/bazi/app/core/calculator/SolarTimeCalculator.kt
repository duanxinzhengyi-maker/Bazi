package com.bazi.app.core.calculator

import kotlinx.datetime.*
import kotlin.math.*

/**
 * 真太阳时计算器
 */
object SolarTimeCalculator {
    
    /**
     * 计算真太阳时
     * @param localDateTime 当地时间
     * @param longitude 经度
     * @param timeZone 时区
     * @param isDaylightSaving 是否夏令时
     * @return 真太阳时
     */
    fun calculateSolarTime(
        localDateTime: LocalDateTime,
        longitude: Double,
        timeZone: String,
        isDaylightSaving: Boolean
    ): LocalDateTime {
        // 转换为UTC时间
        val timeZoneOffset = TimeZone.of(timeZone)
        val instant = localDateTime.toInstant(timeZoneOffset)
        
        // 夏令时调整
        val adjustedInstant = if (isDaylightSaving) {
            instant.minus(1, DateTimeUnit.HOUR)
        } else {
            instant
        }
        
        // 计算儒略日
        val julianDay = calculateJulianDay(adjustedInstant)
        
        // 计算时差
        val equationOfTime = calculateEquationOfTime(julianDay)
        
        // 计算经度时差（分钟）
        val longitudeCorrection = (longitude - getStandardMeridian(timeZone)) * 4.0
        
        // 总时差（分钟）
        val totalCorrection = equationOfTime + longitudeCorrection
        
        // 应用时差得到真太阳时
        val correctionMinutes = totalCorrection.toInt()
        val timeZoneObj = TimeZone.of(timeZone)
        val localInstant = localDateTime.toInstant(timeZoneObj)
        val correctedInstant = localInstant.plus(correctionMinutes, DateTimeUnit.MINUTE, timeZoneObj)
        return correctedInstant.toLocalDateTime(timeZoneObj)
    }
    
    /**
     * 计算儒略日
     */
    private fun calculateJulianDay(instant: Instant): Double {
        val epochDay = instant.epochSeconds / 86400.0
        return epochDay + 2440587.5 // Unix epoch to Julian Day conversion
    }
    
    /**
     * 计算时差方程（分钟）
     */
    private fun calculateEquationOfTime(julianDay: Double): Double {
        val n = julianDay - 2451545.0
        val l = (280.460 + 0.9856474 * n) % 360.0
        val g = Math.toRadians((357.528 + 0.9856003 * n) % 360.0)
        val lambda = Math.toRadians(l + 1.915 * sin(g) + 0.020 * sin(2 * g))
        
        val alpha = atan2(cos(Math.toRadians(23.439)) * sin(lambda), cos(lambda))
        val equationOfTime = 4.0 * Math.toDegrees(Math.toRadians(l) - alpha)
        
        return equationOfTime
    }
    
    /**
     * 获取时区的标准经线
     */
    private fun getStandardMeridian(timeZone: String): Double {
        return when {
            timeZone.contains("+08") || timeZone.contains("Asia/Shanghai") -> 120.0
            timeZone.contains("+09") -> 135.0
            timeZone.contains("+07") -> 105.0
            timeZone.contains("-05") -> -75.0
            timeZone.contains("-08") -> -120.0
            else -> 120.0 // 默认东八区
        }
    }
}