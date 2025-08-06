package com.bazi.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bazi.app.core.calculator.BaziCalculator
import com.bazi.app.data.model.BaziChart
import com.bazi.app.data.model.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 八字排盘ViewModel
 */
class BaziChartViewModel : ViewModel() {
    
    // UI状态
    private val _uiState = MutableStateFlow(BaziChartUiState())
    val uiState: StateFlow<BaziChartUiState> = _uiState.asStateFlow()
    
    // 八字排盘结果
    private val _baziChart = MutableStateFlow<BaziChart?>(null)
    val baziChart: StateFlow<BaziChart?> = _baziChart.asStateFlow()
    
    // 选中的流年
    private val _selectedLiunianYear = MutableStateFlow<Int?>(null)
    val selectedLiunianYear: StateFlow<Int?> = _selectedLiunianYear.asStateFlow()
    
    /**
     * 计算八字排盘
     */
    fun calculateBaziChart(profile: Profile) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                val chart = BaziCalculator.calculateBaziChart(profile)
                _baziChart.value = chart
                
                // 设置当前年份为默认选中的流年
                val currentYear = chart.liunian.find { it.isCurrent }?.year
                _selectedLiunianYear.value = currentYear
                
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "排盘计算失败: ${e.message}"
                )
            }
        }
    }
    
    /**
     * 选择流年
     */
    fun selectLiunianYear(year: Int) {
        _selectedLiunianYear.value = year
    }
    
    /**
     * 获取选中流年的流月
     */
    fun getSelectedYearLiuyue(): List<com.bazi.app.data.model.Liuyue> {
        val chart = _baziChart.value ?: return emptyList()
        // 这里应该根据选中的年份重新计算流月，简化处理返回当前流月
        return chart.liuyue
    }
    
    /**
     * 清除错误信息
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

/**
 * 八字排盘UI状态
 */
data class BaziChartUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)