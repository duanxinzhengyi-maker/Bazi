package com.bazi.app.data.model

import kotlinx.datetime.LocalDateTime

/**
 * 八字排盘结果数据模型
 */
data class BaziChart(
    val profile: Profile,
    val solarTime: LocalDateTime,  // 真太阳时
    val fourPillars: FourPillars,  // 四柱
    val dayun: List<Dayun>,        // 大运
    val liunian: List<Liunian>,    // 流年
    val liuyue: List<Liuyue>       // 流月
)

/**
 * 四柱八字
 */
data class FourPillars(
    val year: Pillar,   // 年柱
    val month: Pillar,  // 月柱
    val day: Pillar,    // 日柱
    val hour: Pillar    // 时柱
)

/**
 * 单柱信息
 */
data class Pillar(
    val heavenlyStem: HeavenlyStem,  // 天干
    val earthlyBranch: EarthlyBranch, // 地支
    val hiddenStems: List<HeavenlyStem>, // 藏干
    val tenGods: List<TenGod>,       // 十神
    val nayin: String,               // 纳音
    val shensha: List<String>,       // 神煞
    val twelveStates: TwelveStates   // 十二长生
)

/**
 * 大运
 */
data class Dayun(
    val startAge: Int,              // 起始年龄
    val startYear: Int,             // 起始公历年份
    val heavenlyStem: HeavenlyStem, // 天干
    val earthlyBranch: EarthlyBranch, // 地支
    val tenGod: TenGod,             // 十神
    val isCurrent: Boolean = false   // 是否当前大运
)

/**
 * 流年
 */
data class Liunian(
    val year: Int,                  // 公历年份
    val heavenlyStem: HeavenlyStem, // 天干
    val earthlyBranch: EarthlyBranch, // 地支
    val tenGod: TenGod,             // 十神
    val isCurrent: Boolean = false   // 是否当前年份
)

/**
 * 流月
 */
data class Liuyue(
    val month: Int,                 // 月份
    val solarTerm: String,          // 节气
    val gregorianDate: String,      // 大致公历日期
    val heavenlyStem: HeavenlyStem, // 天干
    val earthlyBranch: EarthlyBranch, // 地支
    val tenGod: TenGod,             // 十神
    val isCurrent: Boolean = false   // 是否当前月份
)

/**
 * 天干枚举
 */
enum class HeavenlyStem(val chinese: String, val index: Int) {
    JIA("甲", 0), YI("乙", 1), BING("丙", 2), DING("丁", 3), WU("戊", 4),
    JI("己", 5), GENG("庚", 6), XIN("辛", 7), REN("壬", 8), GUI("癸", 9)
}

/**
 * 地支枚举
 */
enum class EarthlyBranch(val chinese: String, val index: Int) {
    ZI("子", 0), CHOU("丑", 1), YIN("寅", 2), MAO("卯", 3),
    CHEN("辰", 4), SI("巳", 5), WU("午", 6), WEI("未", 7),
    SHEN("申", 8), YOU("酉", 9), XU("戌", 10), HAI("亥", 11)
}

/**
 * 十神枚举
 */
enum class TenGod(val chinese: String) {
    BI_JIAN("比肩"), JIE_CAI("劫财"), SHI_SHEN("食神"), SHANG_GUAN("伤官"),
    PIAN_CAI("偏财"), ZHENG_CAI("正财"), QI_SHA("七杀"), ZHENG_GUAN("正官"),
    PIAN_YIN("偏印"), ZHENG_YIN("正印")
}

/**
 * 十二长生枚举
 */
enum class TwelveStates(val chinese: String) {
    CHANG_SHENG("长生"), MU_YU("沐浴"), GUAN_DAI("冠带"), LIN_GUAN("临官"),
    DI_WANG("帝旺"), SHUAI("衰"), BING("病"), SI("死"),
    MU("墓"), JUE("绝"), TAI("胎"), YANG("养")
}