# 八字排盘 (Bazi)

一款专业的移动端八字排盘应用，基于真太阳时精确计算，集成Time4A天文算法库，为用户提供详细的命理分析。

## 📱 项目概述

本应用是一款Android原生八字排盘工具，核心功能包括：
- 个人档案管理系统
- 基于真太阳时的精确八字排盘
- 集成Time4A库的专业节气和农历计算
- 四柱、大运、流年、流月详细展示
- 专业级命理计算引擎

## 🎯 项目状态

**当前版本**: v1.0 (开发中)
**构建状态**: ✅ 编译通过
**核心功能**: ✅ 已实现
**测试状态**: 🧪 待完善

## ✨ 功能特性

### 🔮 核心功能
- **精确八字排盘**: 基于真太阳时计算，确保时辰准确性
- **专业节气计算**: 集成Time4A库，提供精确的二十四节气计算
- **农历转换**: 支持公历与农历的精确互转
- **个人档案管理**: 支持多人档案存储和管理
- **详细命盘展示**: 四柱、大运、流年、流月完整显示

### 📊 计算引擎
- **Time4A天文算法**: 集成专业天文计算库，确保计算精度
- **真太阳时修正**: 基于地理位置的精确时间修正
- **节气精算**: 使用天文算法计算精确节气时刻
- **农历算法**: 专业农历计算，支持闰月处理
- **八字推算**: 完整的干支纪年、纪月、纪日、纪时计算

### 📱 数据管理
- **本地数据库**: 使用Room数据库安全存储个人信息
- **档案管理**: 支持个人档案的增删改查
- **数据持久化**: 本地存储，无需网络连接

### 🎨 用户体验
- **Material Design 3**: 现代化UI设计
- **Jetpack Compose**: 声明式UI框架
- **响应式布局**: 适配不同屏幕尺寸
- **直观操作**: 简洁明了的用户界面

### 🗂️ 档案管理
- **档案列表**：清晰展示所有保存的档案信息
- **分类筛选**：支持按分类（自己、家人、朋友、名人、事件）筛选
- **搜索功能**：支持昵称和备注关键词搜索
- **快速选择**：记住上次选择的档案，一键快速排盘

### 📝 档案创建与编辑
- **基本信息**：昵称、性别、分类设置
- **出生信息**：精确到分钟的出生日期时间
- **地理位置**：出生地和现居地的经纬度定位
- **时区设置**：支持全球时区和夏令时配置
- **数据验证**：完整的输入验证和错误提示

### 🔮 八字排盘详情
- **四柱八字**：年、月、日、时四柱的天干地支
- **详细信息**：藏干、十神、纳音、神煞、十二长生
- **大运展示**：10年一步的大运周期，高亮当前大运
- **流年分析**：年度流年干支和十神关系
- **流月详情**：月度流月信息，联动流年显示

### 🧮 核心算法
- **真太阳时计算**：基于地理位置的精确时间换算
- **节气计算**：准确的二十四节气时间点
- **干支排盘**：专业的命理排盘算法
- **大运流年**：完整的时运计算体系

## 🛠️ 技术栈

### 核心框架
- **Kotlin**: 主要开发语言
- **Android SDK**: 目标API 36，最低API 24
- **Jetpack Compose**: 现代化声明式UI框架
- **Material Design 3**: UI设计规范

### 天文计算
- **Time4A (net.time4j)**: 专业天文算法库
  - 精确节气计算
  - 农历历法转换
  - 天文时间处理
- **Kotlinx DateTime**: 基础时间处理

### 数据存储
- **Room Database**: 本地SQLite数据库
- **Kotlin Coroutines**: 异步数据处理

### 架构组件
- **MVVM**: 架构模式
- **ViewModel**: 状态管理
- **Compose Navigation**: 页面导航
- **Dependency Injection**: 依赖注入

### 开发工具
- **Kotlin Kapt**: 注解处理
- **Android Gradle Plugin**: 构建工具
- **ProGuard**: 代码混淆

### 主要依赖
```kotlin
// UI 和 Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.activity:activity-compose")

// 架构组件
implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
implementation("androidx.navigation:navigation-compose")

// 数据存储
implementation("androidx.room:room-runtime")
implementation("androidx.room:room-ktx")

// 时间处理
implementation("org.jetbrains.kotlinx:kotlinx-datetime")
```

## 📁 项目结构

```
app/
├── src/main/java/com/bazi/app/
│   ├── core/                    # 核心计算模块
│   │   └── calculator/          # 专业计算器类
│   │       ├── BaziCalculator.kt        # 八字排盘计算器
│   │       ├── SolarTimeCalculator.kt   # 真太阳时计算器
│   │       ├── SolarTermCalculator.kt   # 节气计算器 (Time4A)
│   │       └── LunarCalendarCalculator.kt # 农历计算器 (Time4A)
│   ├── data/                   # 数据层
│   │   ├── model/              # 数据模型
│   │   │   ├── BaziChart.kt    # 八字命盘模型
│   │   │   └── Profile.kt      # 个人档案模型
│   │   ├── database/           # 数据库相关
│   │   │   ├── BaziDatabase.kt # Room数据库
│   │   │   ├── ProfileDao.kt   # 数据访问对象
│   │   │   └── Converters.kt   # 类型转换器
│   │   └── repository/         # 数据仓库
│   │       └── ProfileRepository.kt # 档案数据仓库
│   ├── ui/                     # UI层 (Jetpack Compose)
│   │   ├── screen/             # 页面组件
│   │   │   ├── ProfileListScreen.kt    # 档案列表页面
│   │   │   ├── ProfileEditScreen.kt    # 档案编辑页面
│   │   │   └── BaziChartScreen.kt      # 八字排盘页面
│   │   ├── viewmodel/          # 视图模型 (MVVM)
│   │   │   ├── ProfileListViewModel.kt
│   │   │   ├── ProfileEditViewModel.kt
│   │   │   └── BaziChartViewModel.kt
│   │   └── theme/              # Material Design 3主题
│   │       ├── Color.kt        # 颜色定义
│   │       ├── Theme.kt        # 主题配置
│   │       └── Type.kt         # 字体样式
│   ├── MainActivity.kt         # 主活动
│   └── BaziApplication.kt      # 应用类
├── build.gradle.kts            # 构建配置
└── proguard-rules.pro          # 混淆规则
```

## 🚀 快速开始

### 环境要求
- **Android Studio**: Hedgehog | 2023.1.1 或更高版本
- **JDK**: 11 或更高版本
- **Android SDK**: API 24-36
- **Kotlin**: 1.9.0 或更高版本
- **Gradle**: 8.0 或更高版本

### 安装步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/your-username/bazi-android.git
   cd bazi-android
   ```

2. **打开项目**
   - 使用 Android Studio 打开项目
   - 等待 Gradle 同步完成
   - 确保所有依赖下载完成

3. **构建项目**
   ```bash
   ./gradlew build
   ```
   
4. **运行应用**
   - 连接 Android 设备或启动模拟器
   - 点击运行按钮或使用快捷键 `Shift + F10`

### 构建状态
- ✅ **编译通过**: 项目已成功编译
- ✅ **依赖完整**: 所有必要依赖已配置
- ✅ **Time4A集成**: 天文算法库已集成

### 构建发布版本
```bash
./gradlew assembleRelease
```

## 🔬 技术特性

### Time4A天文算法库
本项目集成了专业的Time4A天文算法库，提供以下核心功能：

#### 🌟 节气计算
- **精确算法**: 基于天文算法计算二十四节气的精确时刻
- **本地计算**: 无需网络连接，纯本地算法
- **历史支持**: 支持历史年份的节气查询
- **未来预测**: 可计算未来年份的节气时间

#### 🌙 农历系统
- **完整农历**: 支持农历年、月、日的精确转换
- **闰月处理**: 正确处理农历闰月
- **干支纪年**: 完整的天干地支计算
- **生肖推算**: 准确的十二生肖对应

#### ⏰ 时间精度
- **毫秒级精度**: 时间计算精确到毫秒
- **时区支持**: 支持全球时区转换
- **历法兼容**: 支持多种历法系统

## 📖 使用指南

### 1. 创建档案
1. 在主界面点击「+ 新增」按钮
2. 填写完整的个人信息
3. 设置准确的出生时间和地点
4. 保存档案

### 2. 排盘分析
1. 在档案列表中选择目标档案
2. 点击「确定选择」进行排盘
3. 查看详细的八字分析结果

### 3. 管理档案
- 使用搜索框快速查找档案
- 通过分类筛选整理档案
- 点击编辑按钮修改档案信息

### 4. 八字排盘说明
- **四柱显示**: 年柱、月柱、日柱、时柱完整展示
- **精确节气**: 基于Time4A算法的精确节气计算
- **农历信息**: 准确的农历日期和干支信息
- **真太阳时**: 基于地理位置的时间修正
- **专业算法**: 采用天文级别的计算精度

## 🔧 开发说明

### 代码规范
- 遵循 Kotlin 官方编码规范
- 使用 Material Design 3 设计原则
- 采用 MVVM 架构模式
- 函数和类需要添加适当的文档注释

### 核心模块
- **计算引擎**: 集成Time4A库的专业算法
- **数据持久化**: Room数据库本地存储
- **UI框架**: Jetpack Compose声明式UI
- **状态管理**: ViewModel + StateFlow

### 测试策略
- 单元测试覆盖核心计算逻辑
- UI测试覆盖主要用户流程
- Time4A算法验证测试
- 使用 JUnit 和 Compose Testing 框架

### 性能优化
- 本地计算，无网络依赖
- 数据库查询优化
- UI渲染性能优化
- 内存使用优化

## 🚧 开发路线图

### 已完成 ✅
- [x] 项目架构搭建
- [x] Time4A库集成
- [x] 基础UI框架
- [x] 数据库设计
- [x] 核心计算引擎

### 进行中 🔄
- [ ] UI界面完善
- [ ] 功能测试
- [ ] 性能优化

### 计划中 📋
- [ ] 更多节气功能
- [ ] 数据导入导出
- [ ] 主题定制
- [ ] 多语言支持

## 🧪 测试

### 运行单元测试
```bash
./gradlew test
```

### 运行 UI 测试
```bash
./gradlew connectedAndroidTest
```

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

### 提交规范
- 使用清晰的提交信息
- 一个提交只做一件事
- 提交前请运行测试
- 遵循项目代码规范

### 开发流程
1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

- **项目地址**: [GitHub Repository](https://github.com/your-username/bazi-android)
- **问题反馈**: [Issues](https://github.com/your-username/bazi-android/issues)
- **功能建议**: [Discussions](https://github.com/your-username/bazi-android/discussions)

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者和测试者。

## ⚠️ 免责声明

本应用仅供学习、研究和娱乐使用。所有计算结果仅供参考，不应作为重要决策的唯一依据。开发者不对使用本应用产生的任何后果承担责任。

---

**感谢使用八字排盘应用！** 🙏