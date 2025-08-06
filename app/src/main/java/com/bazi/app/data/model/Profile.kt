package com.bazi.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

/**
 * 档案数据模型
 */
@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    // 基本信息
    val nickname: String,           // 昵称
    val gender: Gender,             // 性别
    val category: ProfileCategory,  // 分类
    
    // 出生信息
    val birthDateTime: LocalDateTime,  // 出生日期时间（阳历）
    val timeZone: String,             // 时区
    val isDaylightSaving: Boolean,    // 是否夏令时
    
    // 地理位置
    val birthPlace: String,           // 出生地名称
    val birthLatitude: Double,        // 出生地纬度
    val birthLongitude: Double,       // 出生地经度
    
    val currentPlace: String? = null,     // 现居地名称
    val currentLatitude: Double? = null,  // 现居地纬度
    val currentLongitude: Double? = null, // 现居地经度
    
    // 元数据
    val createdAt: LocalDateTime,     // 创建时间
    val updatedAt: LocalDateTime,     // 更新时间
    val isLastSelected: Boolean = false  // 是否为上次选择的档案
)

/**
 * 性别枚举
 */
enum class Gender(val displayName: String) {
    MALE("男"),
    FEMALE("女")
}

/**
 * 档案分类枚举
 */
enum class ProfileCategory(val displayName: String) {
    SELF("自己"),
    FAMILY("家人"),
    FRIEND("朋友"),
    CELEBRITY("名人"),
    EVENT("事件")
}