package com.bazi.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bazi.app.data.model.Profile
import com.bazi.app.ui.screen.BaziChartScreen
import com.bazi.app.ui.screen.ProfileEditScreen
import com.bazi.app.ui.screen.ProfileListScreen
import com.bazi.app.ui.theme.BaziTheme
import com.bazi.app.ui.viewmodel.BaziChartViewModel
import com.bazi.app.ui.viewmodel.ProfileEditViewModel
import com.bazi.app.ui.viewmodel.ProfileListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaziTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BaziApp()
                }
            }
        }
    }
}

@Composable
fun BaziApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "profile_list"
    ) {
        // 档案列表页面
        composable("profile_list") {
            val viewModel: ProfileListViewModel = viewModel()
            ProfileListScreen(
                viewModel = viewModel,
                onNavigateToEdit = { profileId ->
                    if (profileId != null) {
                        navController.navigate("profile_edit/$profileId")
                    } else {
                        navController.navigate("profile_edit/new")
                    }
                },
                onNavigateToChart = { profile ->
                    // 将Profile对象传递给八字排盘页面
                    // 这里简化处理，实际应用中可能需要更复杂的参数传递
                    navController.navigate("bazi_chart/${profile.id}")
                }
            )
        }
        
        // 档案编辑页面
        composable("profile_edit/{profileId}") { backStackEntry ->
            val profileIdStr = backStackEntry.arguments?.getString("profileId")
            val profileId = if (profileIdStr == "new") null else profileIdStr?.toLongOrNull()
            
            val viewModel: ProfileEditViewModel = viewModel()
            
            ProfileEditScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSaveSuccess = {
                    navController.popBackStack()
                }
            )
        }
        
        // 八字排盘页面
        composable("bazi_chart/{profileId}") { backStackEntry ->
            val profileId = backStackEntry.arguments?.getString("profileId")?.toLongOrNull()
            
            if (profileId != null) {
                val chartViewModel: BaziChartViewModel = viewModel()
                val listViewModel: ProfileListViewModel = viewModel()
                
                // 这里需要根据profileId获取Profile对象
                // 简化处理，实际应用中应该从数据库获取
                val profile = Profile(
                    id = profileId,
                    nickname = "测试用户",
                    gender = com.bazi.app.data.model.Gender.MALE,
                    category = com.bazi.app.data.model.ProfileCategory.FAMILY,
                    birthDateTime = kotlinx.datetime.LocalDateTime(2000, 1, 1, 8, 30),
                    timeZone = "Asia/Shanghai",
                    isDaylightSaving = false,
                    birthPlace = "北京市",
                    birthLatitude = 39.9042,
                    birthLongitude = 116.4074,
                    currentPlace = "北京市",
                    currentLatitude = 39.9042,
                    currentLongitude = 116.4074,
                    createdAt = kotlinx.datetime.LocalDateTime(2024, 1, 1, 0, 0),
                    updatedAt = kotlinx.datetime.LocalDateTime(2024, 1, 1, 0, 0)
                )
                
                BaziChartScreen(
                    profile = profile,
                    viewModel = chartViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}