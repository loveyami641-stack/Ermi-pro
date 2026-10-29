package com.example.ermipro.calendar

import java.util.Calendar

data class EthDate(
    val year: Int,
    val month: Int, // 1 to 13
    val day: Int    // 1 to 30 (or 1 to 5/6 for Pagume)
) : Comparable<EthDate> {
    override fun compareTo(other: EthDate): Int {
        if (year != other.year) return year.compareTo(other.year)
        if (month != other.month) return month.compareTo(other.month)
        return day.compareTo(other.day)
    }

    fun toDisplayString(showAmharic: Boolean = false): String {
        val monthName = if (showAmharic) {
            EthiopianCalendarHelper.ETH_MONTH_NAMES_AMHARIC.getOrElse(month - 1) { "ወር $month" }
        } else {
            EthiopianCalendarHelper.ETH_MONTH_NAMES[month - 1]
        }
        return "$monthName $day, $year"
    }

    fun toShortString(): String = String.format("%02d/%02d/%04d", day, month, year)
}

data class GregDate(
    val year: Int,
    val month: Int, // 1 to 12
    val day: Int    // 1 to 31
) : Comparable<GregDate> {
    override fun compareTo(other: GregDate): Int {
        if (year != other.year) return year.compareTo(other.year)
        if (month != other.month) return month.compareTo(other.month)
        return day.compareTo(other.day)
    }

    fun toDisplayString(): String {
        val monthNames = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )
        val mName = monthNames.getOrElse(month - 1) { "$month" }
        return "$mName $day, $year"
    }
}

object EthiopianCalendarHelper {

    val ETH_MONTH_NAMES = listOf(
        "Meskerem",
        "Tikimt",
        "Hidar",
        "Tahsas",
        "Tir",
        "Yekatit",
        "Megabit",
        "Miazia",
        "Ginbot",
        "Sene",
        "Hamle",
        "Nehase",
        "Pagume"
    )

    val ETH_MONTH_NAMES_AMHARIC = listOf(
        "መስከረም",
        "ጥቅምት",
        "ኅዳር",
        "ታኅሣሥ",
        "ጥር",
        "የካቲት",
        "መጋቢት",
        "ሚያዝያ",
        "ግንቦት",
        "ሰኔ",
        "ሐምሌ",
        "ነሐሴ",
        "ጳጉሜ"
    )

    val WEEKDAY_NAMES_SHORT = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val WEEKDAY_NAMES_AMHARIC = listOf("ሰኞ", "ማክሰኞ", "ረቡዕ", "ሐሙስ", "አርብ", "ቅዳሜ", "እሑድ")

    fun isEthLeapYear(year: Int): Boolean = (year % 4 == 3)

    fun getDaysInEthMonth(year: Int, month: Int): Int {
        return when (month) {
            in 1..12 -> 30
            13 -> if (isEthLeapYear(year)) 6 else 5
            else -> 30
        }
    }

    /**
     * Converts a Gregorian date to Julian Day Number (JDN)
     */
    fun gregorianToJdn(year: Int, month: Int, day: Int): Long {
        val a = (14 - month) / 12
        val y = year + 4800 - a
        val m = month + 12 * a - 3
        return (day + (153 * m + 2) / 5 + 365L * y + y / 4 - y / 100 + y / 400 - 32045)
    }

    /**
     * Converts Julian Day Number (JDN) to a Gregorian date
     */
    fun jdnToGregorian(jdn: Long): GregDate {
        val a = jdn + 32044
        val b = (4 * a + 3) / 146097
        val c = a - (146097 * b) / 4
        val d = (4 * c + 3) / 1461
        val e = c - (1461 * d) / 4
        val m = (5 * e + 2) / 153
        val day = (e - (153 * m + 2) / 5 + 1).toInt()
        val month = (m + 3 - 12 * (m / 10)).toInt()
        val year = (100 * b + d - 4800 + (m / 10)).toInt()
        return GregDate(year, month, day)
    }

    /**
     * Converts Gregorian date to Ethiopian date with mathematically proven exact algorithm.
     */
    fun gregorianToEth(year: Int, month: Int, day: Int): EthDate {
        val jdn = gregorianToJdn(year, month, day)
        val r = (jdn - 1723856) % 1461
        val n = (r % 365) + 365 * (r / 1460)
        val ethYear = (4 * ((jdn - 1723856) / 1461) + (r / 365) - (r / 1460)).toInt()
        val ethMonth = ((n / 30) + 1).toInt()
        val ethDay = ((n % 30) + 1).toInt()
        return EthDate(ethYear, ethMonth, ethDay)
    }

    /**
     * Converts Ethiopian date to Julian Day Number (JDN)
     */
    fun ethToJdn(year: Int, month: Int, day: Int): Long {
        return (1723856L + 364L) + 365L * (year - 1) + (year / 4) + 30L * (month - 1) + day
    }

    /**
     * Converts Ethiopian date to Gregorian date.
     */
    fun ethToGregorian(year: Int, month: Int, day: Int): GregDate {
        val jdn = ethToJdn(year, month, day)
        return jdnToGregorian(jdn)
    }

    /**
     * Calculates day of week: 0 = Monday, 1 = Tuesday, ..., 6 = Sunday.
     */
    fun getDayOfWeek(ethYear: Int, ethMonth: Int, ethDay: Int): Int {
        val jdn = ethToJdn(ethYear, ethMonth, ethDay)
        // JDN 0 was a Monday -> (jdn % 7) gives Monday=0 .. Sunday=6
        return ((jdn % 7 + 7) % 7).toInt()
    }

    fun getTodayGreg(): GregDate {
        val cal = Calendar.getInstance()
        return GregDate(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun getTodayEth(): EthDate {
        val greg = getTodayGreg()
        return gregorianToEth(greg.year, greg.month, greg.day)
    }
}
