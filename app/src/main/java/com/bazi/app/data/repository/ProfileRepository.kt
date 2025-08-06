package com.bazi.app.data.repository

import com.bazi.app.data.database.ProfileDao
import com.bazi.app.data.model.Profile
import com.bazi.app.data.model.ProfileCategory
import kotlinx.coroutines.flow.Flow

/**
 * 档案仓库
 */
class ProfileRepository(private val profileDao: ProfileDao) {
    
    /**
     * 获取所有档案
     */
    fun getAllProfiles(): Flow<List<Profile>> = profileDao.getAllProfiles()
    
    /**
     * 根据ID获取档案
     */
    suspend fun getProfileById(id: Long): Profile? = profileDao.getProfileById(id)
    
    /**
     * 根据分类筛选档案
     */
    fun getProfilesByCategory(category: ProfileCategory): Flow<List<Profile>> = 
        profileDao.getProfilesByCategory(category)
    
    /**
     * 搜索档案
     */
    fun searchProfiles(keyword: String): Flow<List<Profile>> = 
        profileDao.searchProfiles(keyword)
    
    /**
     * 根据分类和关键词搜索档案
     */
    fun searchProfilesByCategory(category: ProfileCategory, keyword: String): Flow<List<Profile>> = 
        profileDao.searchProfilesByCategory(category, keyword)
    
    /**
     * 获取上次选择的档案
     */
    suspend fun getLastSelectedProfile(): Profile? = profileDao.getLastSelectedProfile()
    
    /**
     * 插入档案
     */
    suspend fun insertProfile(profile: Profile): Long = profileDao.insertProfile(profile)
    
    /**
     * 更新档案
     */
    suspend fun updateProfile(profile: Profile) = profileDao.updateProfile(profile)
    
    /**
     * 删除档案
     */
    suspend fun deleteProfile(profile: Profile) = profileDao.deleteProfile(profile)
    
    /**
     * 设置上次选择的档案
     */
    suspend fun setLastSelectedProfile(profileId: Long) = 
        profileDao.setLastSelectedProfile(profileId)
}