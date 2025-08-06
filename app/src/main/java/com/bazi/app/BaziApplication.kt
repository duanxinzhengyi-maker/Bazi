package com.bazi.app

import android.app.Application
import net.time4j.android.ApplicationStarter

/**
 * 八字排盘应用程序类
 * 负责初始化Time4A库以支持精确的农历和节气计算
 */
class BaziApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // 初始化Time4A库，启用后台预加载以提高性能
        ApplicationStarter.initialize(this, true)
    }
}