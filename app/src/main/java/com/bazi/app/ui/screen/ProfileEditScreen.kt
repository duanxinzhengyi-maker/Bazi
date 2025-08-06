package com.bazi.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bazi.app.data.model.Gender
import com.bazi.app.data.model.ProfileCategory
import com.bazi.app.ui.viewmodel.ProfileEditViewModel

/**
 * 档案编辑界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    viewModel: ProfileEditViewModel,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 顶部导航栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "返回")
            }
            
            Text(
                text = "档案编辑",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            
            // 暂时隐藏删除按钮，等待完善编辑逻辑
            if (false) {
                TextButton(
                    onClick = { showDeleteDialog = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("删除")
                }
            }
        }
        
        // 表单内容
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 昵称
            OutlinedTextField(
                value = formState.nickname,
                onValueChange = viewModel::updateNickname,
                label = { Text("昵称") },
                placeholder = { Text("请输入昵称") },
                modifier = Modifier.fillMaxWidth()
            )
            
            // 性别
            Column {
                Text(
                    text = "性别",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Gender.values().forEach { gender ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.selectable(
                                selected = formState.gender == gender,
                                onClick = { viewModel.updateGender(gender) }
                            )
                        ) {
                            RadioButton(
                                selected = formState.gender == gender,
                                onClick = { viewModel.updateGender(gender) }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(gender.displayName)
                        }
                    }
                }
            }
            
            // 分类
            Box {
                OutlinedTextField(
                    value = formState.category.displayName,
                    onValueChange = { },
                    label = { Text("分类") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showCategoryDropdown = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                DropdownMenu(
                    expanded = showCategoryDropdown,
                    onDismissRequest = { showCategoryDropdown = false }
                ) {
                    ProfileCategory.values().forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.displayName) },
                            onClick = {
                                viewModel.updateCategory(category)
                                showCategoryDropdown = false
                            }
                        )
                    }
                }
            }
            
            // 出生日期时间
            OutlinedTextField(
                value = formState.birthDateTime.toString(),
                onValueChange = { /* TODO: 实现日期时间选择器 */ },
                label = { Text("出生日期时间") },
                placeholder = { Text("2000-01-01 08:30") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
            
            // 夏令时开关
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = formState.isDaylightSaving,
                    onCheckedChange = viewModel::updateDaylightSaving
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("夏令时")
                    Text(
                        text = "夏令时期间，时间会提前1小时，请根据实际情况选择",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // 出生地
            OutlinedTextField(
                value = formState.birthPlace,
                onValueChange = { /* TODO: 实现地点选择器 */ },
                label = { Text("出生地") },
                placeholder = { Text("北京市北京市东城区") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text("经纬度 ${formState.birthLongitude}°E ${formState.birthLatitude}°N")
                }
            )
            
            // 现居地
            OutlinedTextField(
                value = formState.currentPlace,
                onValueChange = { /* TODO: 实现地点选择器 */ },
                label = { Text("现居地") },
                placeholder = { Text("北京市北京市东城区") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text("经纬度 ${formState.currentLongitude ?: 0.0}°E ${formState.currentLatitude ?: 0.0}°N")
                }
            )
        }
        
        // 底部保存按钮
        Button(
            onClick = {
                viewModel.saveProfile()
            },
            enabled = !uiState.isLoading && formState.isValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text("保存档案")
        }
    }
    
    // 删除确认对话框
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("删除档案") },
            text = { Text("确定要删除此档案吗？此操作不可撤销。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteProfile()
                        showDeleteDialog = false
                    }
                ) {
                    Text("删除")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
    
    // 监听保存成功状态
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onSaveSuccess()
        }
    }
    
    // 监听删除成功状态
    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            onNavigateBack()
        }
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