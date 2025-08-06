package com.bazi.app.core.calculator

import com.bazi.app.data.model.*
import kotlinx.datetime.*

/**
 * 八字排盘计算器
 */
object BaziCalculator {
    
    /**
     * 计算完整的八字排盘
     */
    fun calculateBaziChart(profile: Profile): BaziChart {
        // 计算真太阳时
        val solarTime = SolarTimeCalculator.calculateSolarTime(
            profile.birthDateTime,
            profile.birthLongitude,
            profile.timeZone,
            profile.isDaylightSaving
        )
        
        // 计算四柱
        val fourPillars = calculateFourPillars(solarTime)
        
        // 计算大运
        val dayun = calculateDayun(fourPillars, profile.gender, solarTime)
        
        // 计算流年
        val liunian = calculateLiunian(fourPillars.day.heavenlyStem)
        
        // 计算流月
        val liuyue = calculateLiuyue(fourPillars.day.heavenlyStem, Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year)
        
        return BaziChart(
            profile = profile,
            solarTime = solarTime,
            fourPillars = fourPillars,
            dayun = dayun,
            liunian = liunian,
            liuyue = liuyue
        )
    }
    
    /**
     * 计算四柱八字
     */
    private fun calculateFourPillars(solarTime: LocalDateTime): FourPillars {
        val year = solarTime.year
        val month = solarTime.monthNumber
        val day = solarTime.dayOfMonth
        val hour = solarTime.hour
        
        // 计算年柱
        val yearPillar = calculateYearPillar(year)
        
        // 计算月柱
        val monthPillar = calculateMonthPillar(year, month, yearPillar.heavenlyStem)
        
        // 计算日柱
        val dayPillar = calculateDayPillar(year, month, day)
        
        // 计算时柱
        val hourPillar = calculateHourPillar(hour, dayPillar.heavenlyStem)
        
        return FourPillars(yearPillar, monthPillar, dayPillar, hourPillar)
    }
    
    /**
     * 计算年柱
     */
    private fun calculateYearPillar(year: Int): Pillar {
        // 简化计算，实际应该考虑立春节气
        val stemIndex = (year - 4) % 10
        val branchIndex = (year - 4) % 12
        
        val heavenlyStem = HeavenlyStem.values()[stemIndex]
        val earthlyBranch = EarthlyBranch.values()[branchIndex]
        
        return createPillar(heavenlyStem, earthlyBranch)
    }
    
    /**
     * 计算月柱
     */
    private fun calculateMonthPillar(year: Int, month: Int, yearStem: HeavenlyStem): Pillar {
        // 简化计算，实际应该根据节气确定月份
        val monthBranchIndex = (month + 1) % 12
        val monthStemIndex = (yearStem.index * 2 + month - 1) % 10
        
        val heavenlyStem = HeavenlyStem.values()[monthStemIndex]
        val earthlyBranch = EarthlyBranch.values()[monthBranchIndex]
        
        return createPillar(heavenlyStem, earthlyBranch)
    }
    
    /**
     * 计算日柱
     */
    private fun calculateDayPillar(year: Int, month: Int, day: Int): Pillar {
        // 使用简化的日柱计算方法
        val totalDays = calculateTotalDays(year, month, day)
        val stemIndex = (totalDays - 1) % 10
        val branchIndex = (totalDays - 1) % 12
        
        val heavenlyStem = HeavenlyStem.values()[stemIndex]
        val earthlyBranch = EarthlyBranch.values()[branchIndex]
        
        return createPillar(heavenlyStem, earthlyBranch)
    }
    
    /**
     * 计算时柱
     */
    private fun calculateHourPillar(hour: Int, dayStem: HeavenlyStem): Pillar {
        val hourBranchIndex = when (hour) {
            in 23..24, in 0..0 -> 0  // 子时
            in 1..2 -> 1             // 丑时
            in 3..4 -> 2             // 寅时
            in 5..6 -> 3             // 卯时
            in 7..8 -> 4             // 辰时
            in 9..10 -> 5            // 巳时
            in 11..12 -> 6           // 午时
            in 13..14 -> 7           // 未时
            in 15..16 -> 8           // 申时
            in 17..18 -> 9           // 酉时
            in 19..20 -> 10          // 戌时
            in 21..22 -> 11          // 亥时
            else -> 0
        }
        
        val hourStemIndex = (dayStem.index * 2 + hourBranchIndex) % 10
        
        val heavenlyStem = HeavenlyStem.values()[hourStemIndex]
        val earthlyBranch = EarthlyBranch.values()[hourBranchIndex]
        
        return createPillar(heavenlyStem, earthlyBranch)
    }
    
    /**
     * 创建柱信息
     */
    private fun createPillar(heavenlyStem: HeavenlyStem, earthlyBranch: EarthlyBranch): Pillar {
        val hiddenStems = getHiddenStems(earthlyBranch)
        val tenGods = listOf(TenGod.BI_JIAN) // 简化处理
        val nayin = getNayin(heavenlyStem, earthlyBranch)
        val shensha = listOf<String>() // 简化处理
        val twelveStates = TwelveStates.CHANG_SHENG // 简化处理
        
        return Pillar(heavenlyStem, earthlyBranch, hiddenStems, tenGods, nayin, shensha, twelveStates)
    }
    
    /**
     * 获取地支藏干
     */
    private fun getHiddenStems(earthlyBranch: EarthlyBranch): List<HeavenlyStem> {
        return when (earthlyBranch) {
            EarthlyBranch.ZI -> listOf(HeavenlyStem.GUI)
            EarthlyBranch.CHOU -> listOf(HeavenlyStem.JI, HeavenlyStem.GUI, HeavenlyStem.XIN)
            EarthlyBranch.YIN -> listOf(HeavenlyStem.JIA, HeavenlyStem.BING, HeavenlyStem.WU)
            EarthlyBranch.MAO -> listOf(HeavenlyStem.YI)
            EarthlyBranch.CHEN -> listOf(HeavenlyStem.WU, HeavenlyStem.YI, HeavenlyStem.GUI)
            EarthlyBranch.SI -> listOf(HeavenlyStem.BING, HeavenlyStem.GENG, HeavenlyStem.WU)
            EarthlyBranch.WU -> listOf(HeavenlyStem.DING, HeavenlyStem.JI)
            EarthlyBranch.WEI -> listOf(HeavenlyStem.JI, HeavenlyStem.DING, HeavenlyStem.YI)
            EarthlyBranch.SHEN -> listOf(HeavenlyStem.GENG, HeavenlyStem.REN, HeavenlyStem.WU)
            EarthlyBranch.YOU -> listOf(HeavenlyStem.XIN)
            EarthlyBranch.XU -> listOf(HeavenlyStem.WU, HeavenlyStem.XIN, HeavenlyStem.DING)
            EarthlyBranch.HAI -> listOf(HeavenlyStem.REN, HeavenlyStem.JIA)
        }
    }
    
    /**
     * 获取纳音
     */
    private fun getNayin(heavenlyStem: HeavenlyStem, earthlyBranch: EarthlyBranch): String {
        // 简化的纳音表
        val nayinTable = arrayOf(
            arrayOf("海中金", "炉中火", "大林木", "路旁土", "剑锋金", "山头火"),
            arrayOf("涧下水", "城头土", "白蜡金", "杨柳木", "泉中水", "屋上土"),
            arrayOf("霹雳火", "松柏木", "长流水", "沙中金", "山下火", "平地木"),
            arrayOf("壁上土", "金箔金", "覆灯火", "天河水", "大驿土", "钗钏金"),
            arrayOf("桑柘木", "大溪水", "沙中土", "天上火", "石榴木", "大海水")
        )
        
        val index = (heavenlyStem.index + earthlyBranch.index) % 30
        return nayinTable[index / 6][index % 6]
    }
    
    /**
     * 计算总天数（用于日柱计算）
     */
    private fun calculateTotalDays(year: Int, month: Int, day: Int): Int {
        // 简化计算，实际应该更精确
        var totalDays = 0
        for (y in 1900 until year) {
            totalDays += if (isLeapYear(y)) 366 else 365
        }
        
        val daysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        if (isLeapYear(year)) daysInMonth[1] = 29
        
        for (m in 1 until month) {
            totalDays += daysInMonth[m - 1]
        }
        
        totalDays += day
        return totalDays
    }
    
    /**
     * 判断是否闰年
     */
    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }
    
    /**
     * 计算大运
     */
    private fun calculateDayun(fourPillars: FourPillars, gender: Gender, birthTime: LocalDateTime): List<Dayun> {
        val dayuns = mutableListOf<Dayun>()
        val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
        val birthYear = birthTime.year
        
        // 简化的大运计算
        for (i in 0..9) {
            val startAge = i * 10 + 8
            val startYear = birthYear + startAge
            val stemIndex = (fourPillars.month.heavenlyStem.index + i) % 10
            val branchIndex = (fourPillars.month.earthlyBranch.index + i) % 12
            
            val dayun = Dayun(
                startAge = startAge,
                startYear = startYear,
                heavenlyStem = HeavenlyStem.values()[stemIndex],
                earthlyBranch = EarthlyBranch.values()[branchIndex],
                tenGod = TenGod.BI_JIAN, // 简化处理
                isCurrent = currentYear in startYear until (startYear + 10)
            )
            dayuns.add(dayun)
        }
        
        return dayuns
    }
    
    /**
     * 计算流年
     */
    private fun calculateLiunian(dayStem: HeavenlyStem): List<Liunian> {
        val liunians = mutableListOf<Liunian>()
        val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
        
        for (year in (currentYear - 5)..(currentYear + 5)) {
            val stemIndex = (year - 4) % 10
            val branchIndex = (year - 4) % 12
            
            val liunian = Liunian(
                year = year,
                heavenlyStem = HeavenlyStem.values()[stemIndex],
                earthlyBranch = EarthlyBranch.values()[branchIndex],
                tenGod = TenGod.BI_JIAN, // 简化处理
                isCurrent = year == currentYear
            )
            liunians.add(liunian)
        }
        
        return liunians
    }
    
    /**
     * 计算流月
     */
    private fun calculateLiuyue(dayStem: HeavenlyStem, year: Int): List<Liuyue> {
        val liuyues = mutableListOf<Liuyue>()
        val currentMonth = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).monthNumber
        
        val solarTerms = arrayOf(
            "立春", "惊蛰", "清明", "立夏", "芒种", "小暑",
            "立秋", "白露", "寒露", "立冬", "大雪", "小寒"
        )
        
        for (month in 1..12) {
            val stemIndex = (dayStem.index * 2 + month - 1) % 10
            val branchIndex = (month + 1) % 12
            
            val liuyue = Liuyue(
                month = month,
                solarTerm = solarTerms[month - 1],
                gregorianDate = "${month}月",
                heavenlyStem = HeavenlyStem.values()[stemIndex],
                earthlyBranch = EarthlyBranch.values()[branchIndex],
                tenGod = TenGod.BI_JIAN, // 简化处理
                isCurrent = month == currentMonth
            )
            liuyues.add(liuyue)
        }
        
        return liuyues
    }
}