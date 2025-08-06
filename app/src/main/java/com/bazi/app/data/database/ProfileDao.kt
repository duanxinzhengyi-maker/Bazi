package com.bazi.app.data.database

import androidx.room.*
import com.bazi.app.data.model.Profile
import com.bazi.app.data.model.ProfileCategory
import kotlinx.coroutines.flow.Flow

/**
 * 档案数据访问对象
 */
@Dao
interface ProfileDao {
    
    /**
     * 获取所有档案
     */
    @Query("SELECT * FROM profiles ORDER BY updatedAt DESC")
    fun getAllProfiles(): Flow<List<Profile>>
    
    /**
     * 根据ID获取档案
     */
    @Query("SELECT * FROM profiles WHERE id = :id")
    suspend fun getProfileById(id: Long): Profile?
    
    /**
     * 根据分类筛选档案
     */
    @Query("SELECT * FROM profiles WHERE category = :category ORDER BY updatedAt DESC")
    fun getProfilesByCategory(category: ProfileCategory): Flow<List<Profile>>
    
    /**
     * 搜索档案（根据昵称）
     */
    @Query("SELECT * FROM profiles WHERE nickname LIKE '%' || :keyword || '%' ORDER BY updatedAt DESC")
    fun searchProfiles(keyword: String): Flow<List<Profile>>
    
    /**
     * 根据分类和关键词搜索档案
     */
    @Query("SELECT * FROM profiles WHERE category = :category AND nickname LIKE '%' || :keyword || '%' ORDER BY updatedAt DESC")
    fun searchProfilesByCategory(category: ProfileCategory, keyword: String): Flow<List<Profile>>
    
    /**
     * 获取上次选择的档案
     */
    @Query("SELECT * FROM profiles WHERE isLastSelected = 1 LIMIT 1")
    suspend fun getLastSelectedProfile(): Profile?
    
    /**
     * 插入档案
     */
    @Insert
    suspend fun insertProfile(profile: Profile): Long
    
    /**
     * 更新档案
     */
    @Update
    suspend fun updateProfile(profile: Profile)
    
    /**
     * 删除档案
     */
    @Delete
    suspend fun deleteProfile(profile: Profile)
    
    /**
     * 设置上次选择的档案
     */
    @Transaction
    suspend fun setLastSelectedProfile(profileId: Long) {
        clearLastSelected()
        setLastSelected(profileId)
    }
    
    @Query("UPDATE profiles SET isLastSelected = 0")
    suspend fun clearLastSelected()
    
    @Query("UPDATE profiles SET isLastSelected = 1 WHERE id = :profileId")
    suspend fun setLastSelected(profileId: Long)
}