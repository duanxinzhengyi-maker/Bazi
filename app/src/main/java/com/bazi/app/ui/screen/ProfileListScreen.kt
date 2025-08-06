package com.bazi.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bazi.app.data.model.Profile
import com.bazi.app.data.model.ProfileCategory
import com.bazi.app.ui.viewmodel.ProfileListViewModel

/**
 * 档案列表主界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileListScreen(
    viewModel: ProfileListViewModel,
    onNavigateToEdit: (Long?) -> Unit,
    onNavigateToChart: (Profile) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val profiles by viewModel.profiles.collectAsStateWithLifecycle()
    val searchKeyword by viewModel.searchKeyword.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedProfileId by viewModel.selectedProfileId.collectAsStateWithLifecycle()
    
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<Profile?>(null) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 标题
        Text(
            text = "八字工具",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 搜索和筛选区域
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 搜索框
            OutlinedTextField(
                value = searchKeyword,
                onValueChange = viewModel::searchProfiles,
                label = { Text("搜索昵称或备注") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.weight(1f)
            )
            
            // 分类筛选按钮
            Box {
                OutlinedButton(
                    onClick = { showCategoryDropdown = true }
                ) {
                    Text(selectedCategory?.displayName ?: "全部")
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
                
                DropdownMenu(
                    expanded = showCategoryDropdown,
                    onDismissRequest = { showCategoryDropdown = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("全部") },
                        onClick = {
                            viewModel.selectCategory(null)
                            showCategoryDropdown = false
                        }
                    )
                    ProfileCategory.values().forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.displayName) },
                            onClick = {
                                viewModel.selectCategory(category)
                                showCategoryDropdown = false
                            }
                        )
                    }
                }
            }
        }
        
        // 辅助功能按钮
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 上次选择按钮
            OutlinedButton(
                onClick = { /* TODO: 实现上次选择逻辑 */ }
            ) {
                Icon(Icons.Default.Star, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("上次选择")
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 文件夹和同步按钮（预留）
            IconButton(onClick = { /* TODO: 文件夹功能 */ }) {
                Icon(Icons.Default.Menu, contentDescription = "文件夹")
            }
            
            IconButton(onClick = { /* TODO: 同步功能 */ }) {
                Icon(Icons.Default.Refresh, contentDescription = "同步")
            }
        }
        
        // 档案列表
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(profiles) { profile ->
                ProfileListItem(
                    profile = profile,
                    isSelected = profile.id == selectedProfileId,
                    onSelect = { viewModel.selectProfile(profile.id) },
                    onEdit = { onNavigateToEdit(profile.id) },
                    onDelete = { showDeleteDialog = profile }
                )
            }
        }
        
        // 底部操作按钮
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 新增按钮
            OutlinedButton(
                onClick = { onNavigateToEdit(null) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("新增")
            }
            
            // 确定选择按钮
            Button(
                onClick = {
                    val profile = viewModel.confirmSelection()
                    if (profile != null) {
                        onNavigateToChart(profile)
                    }
                },
                enabled = selectedProfileId != null,
                modifier = Modifier.weight(1f)
            ) {
                Text("确定选择")
            }
        }
    }
    
    // 删除确认对话框
    showDeleteDialog?.let { profile ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("删除档案") },
            text = { Text("确定要删除档案\"${profile.nickname}\"吗？此操作不可撤销。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteProfile(profile)
                        showDeleteDialog = null
                    }
                ) {
                    Text("删除")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("取消")
                }
            }
        )
    }
    
    // 显示消息
    uiState.message?.let { message ->
        LaunchedEffect(message) {
            // TODO: 显示Snackbar
            viewModel.clearMessage()
        }
    }
    
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // TODO: 显示错误Snackbar
            viewModel.clearMessage()
        }
    }
}

/**
 * 档案列表项
 */
@Composable
fun ProfileListItem(
    profile: Profile,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onSelect
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 选择框
            RadioButton(
                selected = isSelected,
                onClick = onSelect
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // 档案信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 第一行：昵称
                Text(
                    text = profile.nickname,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // 第二行：分类、出生日期、出生地
                Text(
                    text = "${profile.category.displayName} • ${profile.birthDateTime.date} ${profile.birthDateTime.time} • ${profile.birthPlace}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // 编辑按钮
            TextButton(onClick = onEdit) {
                Text("编辑")
            }
        }
    }
}