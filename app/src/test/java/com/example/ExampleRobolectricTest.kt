package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ermipro.calendar.EthiopianCalendarHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("ERMI PRO", appName)
    }

    @Test
    fun `test Ethiopian Calendar conversions`() {
        // Oct 29, 2026 Gregorian is Tikimt 19, 2019 Ethiopian
        val ethDate = EthiopianCalendarHelper.gregorianToEth(2026, 10, 29)
        assertEquals(2019, ethDate.year)
        assertEquals(2, ethDate.month) // Tikimt is 2nd month
        assertEquals(19, ethDate.day)

        // Roundtrip conversion back to Gregorian
        val gregDate = EthiopianCalendarHelper.ethToGregorian(2019, 2, 19)
        assertEquals(2026, gregDate.year)
        assertEquals(10, gregDate.month)
        assertEquals(29, gregDate.day)

        // Verify Pagume month has 5 or 6 days depending on leap year
        // 2015 is leap year: 2015 % 4 == 3
        assertTrue(EthiopianCalendarHelper.isEthLeapYear(2015))
        assertEquals(6, EthiopianCalendarHelper.getDaysInEthMonth(2015, 13))

        // 2016 is not leap year
        assertEquals(false, EthiopianCalendarHelper.isEthLeapYear(2016))
        assertEquals(5, EthiopianCalendarHelper.getDaysInEthMonth(2016, 13))
    }
}
