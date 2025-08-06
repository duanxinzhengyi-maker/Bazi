package com.bazi.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bazi.app.data.model.Profile
import com.bazi.app.data.model.ProfileCategory
import com.bazi.app.data.repository.ProfileRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * 档案列表ViewModel
 */
class ProfileListViewModel(
    private val repository: ProfileRepository
) : ViewModel() {
    
    // UI状态
    private val _uiState = MutableStateFlow(ProfileListUiState())
    val uiState: StateFlow<ProfileListUiState> = _uiState.asStateFlow()
    
    // 搜索关键词
    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword.asStateFlow()
    
    // 选中的分类
    private val _selectedCategory = MutableStateFlow<ProfileCategory?>(null)
    val selectedCategory: StateFlow<ProfileCategory?> = _selectedCategory.asStateFlow()
    
    // 选中的档案ID
    private val _selectedProfileId = MutableStateFlow<Long?>(null)
    val selectedProfileId: StateFlow<Long?> = _selectedProfileId.asStateFlow()
    
    // 档案列表
    val profiles: StateFlow<List<Profile>> = combine(
        searchKeyword,
        selectedCategory
    ) { keyword, category ->
        when {
            keyword.isNotBlank() && category != null -> 
                repository.searchProfilesByCategory(category, keyword)
            keyword.isNotBlank() -> 
                repository.searchProfiles(keyword)
            category != null -> 
                repository.getProfilesByCategory(category)
            else -> 
                repository.getAllProfiles()
        }
    }.flatMapLatest { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    init {
        loadLastSelectedProfile()
    }
    
    /**
     * 搜索档案
     */
    fun searchProfiles(keyword: String) {
        _searchKeyword.value = keyword
    }
    
    /**
     * 选择分类
     */
    fun selectCategory(category: ProfileCategory?) {
        _selectedCategory.value = category
    }
    
    /**
     * 选择档案
     */
    fun selectProfile(profileId: Long?) {
        _selectedProfileId.value = profileId
    }
    
    /**
     * 确定选择档案
     */
    fun confirmSelection(): Profile? {
        val profileId = _selectedProfileId.value ?: return null
        val profile = profiles.value.find { it.id == profileId }
        
        if (profile != null) {
            viewModelScope.launch {
                repository.setLastSelectedProfile(profileId)
            }
        }
        
        return profile
    }
    
    /**
     * 加载上次选择的档案
     */
    private fun loadLastSelectedProfile() {
        viewModelScope.launch {
            val lastProfile = repository.getLastSelectedProfile()
            if (lastProfile != null) {
                _selectedProfileId.value = lastProfile.id
            }
        }
    }
    
    /**
     * 删除档案
     */
    fun deleteProfile(profile: Profile) {
        viewModelScope.launch {
            try {
                repository.deleteProfile(profile)
                _uiState.value = _uiState.value.copy(
                    message = "档案删除成功"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "删除失败: ${e.message}"
                )
            }
        }
    }
    
    /**
     * 清除消息
     */
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }
}

/**
 * 档案列表UI状态
 */
data class ProfileListUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)