package com.bazi.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bazi.app.data.model.BaziChart
import com.bazi.app.data.model.Profile
import com.bazi.app.ui.viewmodel.BaziChartViewModel

/**
 * 八字排盘显示界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaziChartScreen(
    profile: Profile,
    viewModel: BaziChartViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val baziChart by viewModel.baziChart.collectAsStateWithLifecycle()
    val selectedLiunianYear by viewModel.selectedLiunianYear.collectAsStateWithLifecycle()
    
    // 初始化计算八字
    LaunchedEffect(profile) {
        viewModel.calculateBaziChart(profile)
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部导航栏
        TopAppBar(
            title = { Text(profile.nickname) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                }
            },
            actions = {
                IconButton(onClick = { /* TODO: 分享功能 */ }) {
                    Icon(Icons.Default.Share, contentDescription = "分享")
                }
            }
        )
        
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.calculateBaziChart(profile) }) {
                        Text("重试")
                    }
                }
            }
        } else {
            baziChart?.let { chart ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 基本信息
                    item {
                        ProfileInfoCard(profile = profile, chart = chart)
                    }
                    
                    // 四柱八字
                    item {
                        SizhuCard(chart = chart)
                    }
                    
                    // 大运
                    item {
                        DayunCard(chart = chart)
                    }
                    
                    // 流年选择
                    item {
                        LiunianSelectionCard(
                            chart = chart,
                            selectedYear = selectedLiunianYear,
                            onYearSelected = viewModel::selectLiunianYear
                        )
                    }
                    
                    // 流月
                    item {
                        LiuyueCard(
                            liuyue = viewModel.getSelectedYearLiuyue()
                        )
                    }
                }
            }
        }
    }
}

/**
 * 档案信息卡片
 */
@Composable
fun ProfileInfoCard(profile: Profile, chart: BaziChart) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "基本信息",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("昵称：${profile.nickname}")
                    Text("性别：${profile.gender.displayName}")
                    Text("分类：${profile.category.displayName}")
                }
                Column {
                    Text("出生：${profile.birthDateTime.date} ${profile.birthDateTime.time}")
                    Text("地点：${profile.birthPlace}")
                    Text("真太阳时：${chart.solarTime}")
                }
            }
        }
    }
}

/**
 * 四柱八字卡片
 */
@Composable
fun SizhuCard(chart: BaziChart) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "四柱八字",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 四柱表格
            Column {
                // 表头
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("", "年柱", "月柱", "日柱", "时柱").forEach { header ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = header,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                
                // 天干行
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("天干", fontWeight = FontWeight.Bold)
                    }
                    
                    listOf(
                        chart.fourPillars.year.heavenlyStem.chinese,
                        chart.fourPillars.month.heavenlyStem.chinese,
                        chart.fourPillars.day.heavenlyStem.chinese,
                        chart.fourPillars.hour.heavenlyStem.chinese
                    ).forEach { tiangan ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tiangan,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                
                // 地支行
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("地支", fontWeight = FontWeight.Bold)
                    }
                    
                    listOf(
                        chart.fourPillars.year.earthlyBranch.chinese,
                        chart.fourPillars.month.earthlyBranch.chinese,
                        chart.fourPillars.day.earthlyBranch.chinese,
                        chart.fourPillars.hour.earthlyBranch.chinese
                    ).forEach { dizhi ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dizhi,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                
                // 藏干行
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("藏干", fontWeight = FontWeight.Bold)
                    }
                    
                    listOf(
                        chart.fourPillars.year.hiddenStems.joinToString(" ") { it.chinese },
                        chart.fourPillars.month.hiddenStems.joinToString(" ") { it.chinese },
                        chart.fourPillars.day.hiddenStems.joinToString(" ") { it.chinese },
                        chart.fourPillars.hour.hiddenStems.joinToString(" ") { it.chinese }
                    ).forEach { canggan ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = canggan,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                
                // 纳音行
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("纳音", fontWeight = FontWeight.Bold)
                    }
                    
                    listOf(
                        chart.fourPillars.year.nayin,
                        chart.fourPillars.month.nayin,
                        chart.fourPillars.day.nayin,
                        chart.fourPillars.hour.nayin
                    ).forEach { nayin ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = nayin,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 大运卡片
 */
@Composable
fun DayunCard(chart: BaziChart) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "大运",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chart.dayun) { dayun ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (dayun.isCurrent) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else 
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${dayun.heavenlyStem.chinese}${dayun.earthlyBranch.chinese}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${dayun.startAge}岁起",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 流年选择卡片
 */
@Composable
fun LiunianSelectionCard(
    chart: BaziChart,
    selectedYear: Int?,
    onYearSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "流年",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chart.liunian) { liunian ->
                    Card(
                        modifier = Modifier.selectable(
                            selected = liunian.year == selectedYear,
                            onClick = { onYearSelected(liunian.year) }
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                liunian.year == selectedYear -> MaterialTheme.colorScheme.primary
                                liunian.isCurrent -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.surface
                            }
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${liunian.heavenlyStem.chinese}${liunian.earthlyBranch.chinese}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (liunian.year == selectedYear) 
                                    MaterialTheme.colorScheme.onPrimary 
                                else 
                                    MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${liunian.year}年",
                                fontSize = 12.sp,
                                color = if (liunian.year == selectedYear) 
                                    MaterialTheme.colorScheme.onPrimary 
                                else 
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 流月卡片
 */
@Composable
fun LiuyueCard(liuyue: List<com.bazi.app.data.model.Liuyue>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "流月",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(liuyue) { yue ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (yue.isCurrent) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else 
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${yue.heavenlyStem.chinese}${yue.earthlyBranch.chinese}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${yue.month}月",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}