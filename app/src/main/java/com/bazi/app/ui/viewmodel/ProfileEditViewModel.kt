package com.bazi.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bazi.app.data.model.Gender
import com.bazi.app.data.model.Profile
import com.bazi.app.data.model.ProfileCategory
import com.bazi.app.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * 档案编辑ViewModel
 */
class ProfileEditViewModel(
    private val repository: ProfileRepository
) : ViewModel() {
    
    // UI状态
    private val _uiState = MutableStateFlow(ProfileEditUiState())
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()
    
    // 表单状态
    private val _formState = MutableStateFlow(ProfileFormState())
    val formState: StateFlow<ProfileFormState> = _formState.asStateFlow()
    
    // 当前编辑的档案ID
    private var currentProfileId: Long? = null
    
    /**
     * 加载档案进行编辑
     */
    fun loadProfile(profileId: Long) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val profile = repository.getProfileById(profileId)
                
                if (profile != null) {
                    currentProfileId = profile.id
                    _formState.value = ProfileFormState(
                        nickname = profile.nickname,
                        gender = profile.gender,
                        category = profile.category,
                        birthDateTime = profile.birthDateTime,
                        timeZone = profile.timeZone,
                        isDaylightSaving = profile.isDaylightSaving,
                        birthPlace = profile.birthPlace,
                        birthLatitude = profile.birthLatitude,
                        birthLongitude = profile.birthLongitude,
                        currentPlace = profile.currentPlace ?: "",
                        currentLatitude = profile.currentLatitude,
                        currentLongitude = profile.currentLongitude
                    )
                }
                
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "加载档案失败: ${e.message}"
                )
            }
        }
    }
    
    /**
     * 更新昵称
     */
    fun updateNickname(nickname: String) {
        _formState.value = _formState.value.copy(nickname = nickname)
        validateForm()
    }
    
    /**
     * 更新性别
     */
    fun updateGender(gender: Gender) {
        _formState.value = _formState.value.copy(gender = gender)
        validateForm()
    }
    
    /**
     * 更新分类
     */
    fun updateCategory(category: ProfileCategory) {
        _formState.value = _formState.value.copy(category = category)
        validateForm()
    }
    
    /**
     * 更新出生日期时间
     */
    fun updateBirthDateTime(dateTime: LocalDateTime) {
        _formState.value = _formState.value.copy(birthDateTime = dateTime)
        validateForm()
    }
    
    /**
     * 更新时区
     */
    fun updateTimeZone(timeZone: String) {
        _formState.value = _formState.value.copy(timeZone = timeZone)
        validateForm()
    }
    
    /**
     * 更新夏令时
     */
    fun updateDaylightSaving(isDaylightSaving: Boolean) {
        _formState.value = _formState.value.copy(isDaylightSaving = isDaylightSaving)
        validateForm()
    }
    
    /**
     * 更新出生地
     */
    fun updateBirthPlace(place: String, latitude: Double, longitude: Double) {
        _formState.value = _formState.value.copy(
            birthPlace = place,
            birthLatitude = latitude,
            birthLongitude = longitude
        )
        validateForm()
    }
    
    /**
     * 更新现居地
     */
    fun updateCurrentPlace(place: String, latitude: Double?, longitude: Double?) {
        _formState.value = _formState.value.copy(
            currentPlace = place,
            currentLatitude = latitude,
            currentLongitude = longitude
        )
        validateForm()
    }
    
    /**
     * 保存档案
     */
    fun saveProfile() {
        if (!_formState.value.isValid) {
            _uiState.value = _uiState.value.copy(error = "请填写所有必填项")
            return
        }
        
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                val formState = _formState.value
                
                val profile = if (currentProfileId != null) {
                    // 更新现有档案
                    Profile(
                        id = currentProfileId!!,
                        nickname = formState.nickname,
                        gender = formState.gender,
                        category = formState.category,
                        birthDateTime = formState.birthDateTime,
                        timeZone = formState.timeZone,
                        isDaylightSaving = formState.isDaylightSaving,
                        birthPlace = formState.birthPlace,
                        birthLatitude = formState.birthLatitude,
                        birthLongitude = formState.birthLongitude,
                        currentPlace = formState.currentPlace.takeIf { it.isNotBlank() },
                        currentLatitude = formState.currentLatitude,
                        currentLongitude = formState.currentLongitude,
                        createdAt = now, // 这里应该保持原创建时间，简化处理
                        updatedAt = now
                    )
                } else {
                    // 创建新档案
                    Profile(
                        nickname = formState.nickname,
                        gender = formState.gender,
                        category = formState.category,
                        birthDateTime = formState.birthDateTime,
                        timeZone = formState.timeZone,
                        isDaylightSaving = formState.isDaylightSaving,
                        birthPlace = formState.birthPlace,
                        birthLatitude = formState.birthLatitude,
                        birthLongitude = formState.birthLongitude,
                        currentPlace = formState.currentPlace.takeIf { it.isNotBlank() },
                        currentLatitude = formState.currentLatitude,
                        currentLongitude = formState.currentLongitude,
                        createdAt = now,
                        updatedAt = now
                    )
                }
                
                if (currentProfileId != null) {
                    repository.updateProfile(profile)
                } else {
                    repository.insertProfile(profile)
                }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSaved = true,
                    message = "档案保存成功"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "保存失败: ${e.message}"
                )
            }
        }
    }
    
    /**
     * 删除档案
     */
    fun deleteProfile() {
        val profileId = currentProfileId ?: return
        
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val profile = repository.getProfileById(profileId)
                
                if (profile != null) {
                    repository.deleteProfile(profile)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isDeleted = true,
                        message = "档案删除成功"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "删除失败: ${e.message}"
                )
            }
        }
    }
    
    /**
     * 验证表单
     */
    private fun validateForm() {
        val formState = _formState.value
        val isValid = formState.nickname.isNotBlank() &&
                formState.birthPlace.isNotBlank()
        
        _formState.value = formState.copy(isValid = isValid)
    }
    
    /**
     * 清除消息
     */
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }
    
    /**
     * 重置状态
     */
    fun resetState() {
        _uiState.value = ProfileEditUiState()
        _formState.value = ProfileFormState()
        currentProfileId = null
    }
}

/**
 * 档案编辑UI状态
 */
data class ProfileEditUiState(
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false,
    val message: String? = null,
    val error: String? = null
)

/**
 * 档案表单状态
 */
data class ProfileFormState(
    val nickname: String = "",
    val gender: Gender = Gender.MALE,
    val category: ProfileCategory = ProfileCategory.SELF,
    val birthDateTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val timeZone: String = "+08:00",
    val isDaylightSaving: Boolean = false,
    val birthPlace: String = "",
    val birthLatitude: Double = 39.9042,  // 北京默认坐标
    val birthLongitude: Double = 116.4074,
    val currentPlace: String = "",
    val currentLatitude: Double? = null,
    val currentLongitude: Double? = null,
    val isValid: Boolean = false
)