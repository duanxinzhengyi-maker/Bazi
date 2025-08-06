package com.bazi.app.data.database

import androidx.room.TypeConverter
import com.bazi.app.data.model.Gender
import com.bazi.app.data.model.ProfileCategory
import kotlinx.datetime.LocalDateTime

/**
 * Room数据库类型转换器
 */
class Converters {
    
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.toString()
    }
    
    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.let { LocalDateTime.parse(it) }
    }
    
    @TypeConverter
    fun fromGender(gender: Gender): String {
        return gender.name
    }
    
    @TypeConverter
    fun toGender(genderString: String): Gender {
        return Gender.valueOf(genderString)
    }
    
    @TypeConverter
    fun fromProfileCategory(category: ProfileCategory): String {
        return category.name
    }
    
    @TypeConverter
    fun toProfileCategory(categoryString: String): ProfileCategory {
        return ProfileCategory.valueOf(categoryString)
    }
}