package com.example.ermipro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ermipro.calendar.EthDate
import com.example.ermipro.calendar.EthiopianCalendarHelper
import com.example.ermipro.data.model.BookingEntity
import com.example.ermipro.ui.components.BookingCardCompact
import com.example.ermipro.ui.components.CategoryColors
import com.example.ermipro.ui.components.StatusBadge
import com.example.ermipro.ui.viewmodel.CalendarViewMode
import com.example.ermipro.ui.viewmodel.ErmiViewModel
import com.example.ui.theme.ErmiOrange
import com.example.ui.theme.ErmiOrangeLight
import com.example.ui.theme.ErmiPurple
import com.example.ui.theme.ErmiPurpleContainer
import com.example.ui.theme.ErmiPurpleLight
import com.example.ui.theme.StatusConfirmed
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCardElevated
import com.example.ui.theme.StudioGold
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextSecondary
import com.example.ui.theme.StudioWhite

@Composable
fun CalendarScreen(
    viewModel: ErmiViewModel,
    modifier: Modifier = Modifier
) {
    val selectedEthDate by viewModel.selectedEthDate.collectAsState()
    val calendarMode by viewModel.calendarViewMode.collectAsState()
    val showAmharic by viewModel.showAmharicCalendar.collectAsState()
    val bookings by viewModel.bookings.collectAsState()
    val todayEth = remember { EthiopianCalendarHelper.getTodayEth() }

    val daysInMonth = EthiopianCalendarHelper.getDaysInEthMonth(selectedEthDate.year, selectedEthDate.month)
    val firstDayWeekday = EthiopianCalendarHelper.getDayOfWeek(selectedEthDate.year, selectedEthDate.month, 1) // 0=Mon .. 6=Sun

    val selectedDayBookings = bookings.filter {
        it.ethYear == selectedEthDate.year &&
                it.ethMonth == selectedEthDate.month &&
                it.ethDay == selectedEthDate.day &&
                it.bookingStatus != "Cancelled"
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground),
        contentPadding = PaddingValues(bottom = 95.dp, top = 8.dp, start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Ethiopian Calendar Header & Controls
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, StudioBorder, RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    // Month & Year Title Bar with Navigation
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.navigateMonth(-1) },
                                modifier = Modifier.size(36.dp).testTag("prev_month_button")
                            ) {
                                Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Previous Month", tint = StudioWhite)
                            }

                            Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                                val monthName = if (showAmharic) {
                                    EthiopianCalendarHelper.ETH_MONTH_NAMES_AMHARIC[selectedEthDate.month - 1]
                                } else {
                                    EthiopianCalendarHelper.ETH_MONTH_NAMES[selectedEthDate.month - 1]
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = monthName,
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.Black,
                                        color = StudioWhite
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${selectedEthDate.year} ዓ.ም",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ErmiOrange
                                    )
                                }
                                val gregEquiv = EthiopianCalendarHelper.ethToGregorian(selectedEthDate.year, selectedEthDate.month, 1)
                                Text(
                                    text = "Starts ${gregEquiv.toDisplayString()} • Month ${selectedEthDate.month}/13",
                                    fontSize = 11.sp,
                                    color = StudioTextMuted
                                )
                            }

                            IconButton(
                                onClick = { viewModel.navigateMonth(1) },
                                modifier = Modifier.size(36.dp).testTag("next_month_button")
                            ) {
                                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next Month", tint = StudioWhite)
                            }
                        }

                        // Jump to Today & Language Toggle
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(StudioSurfaceVariant)
                                    .clickable { viewModel.toggleAmharicCalendar() }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (showAmharic) "ENG" else "አማርኛ",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ErmiOrangeLight
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ErmiPurpleContainer)
                                    .clickable { viewModel.setSelectedEthDate(todayEth) }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Today",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    // Calendar View Modes (Month, Week, Day, Agenda)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CalendarViewMode.values().forEach { mode ->
                            val isSelected = calendarMode == mode
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) ErmiOrange else StudioSurfaceVariant)
                                    .clickable { viewModel.setCalendarViewMode(mode) }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = mode.name.lowercase().replaceFirstChar { it.uppercase() },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.Black else StudioWhite
                                )
                            }
                        }
                    }
                }
            }
        }

        // Calendar Content based on Mode
        when (calendarMode) {
            CalendarViewMode.MONTH -> {
                item {
                    MonthCalendarGrid(
                        selectedEthDate = selectedEthDate,
                        daysInMonth = daysInMonth,
                        firstDayWeekday = firstDayWeekday,
                        todayEth = todayEth,
                        showAmharic = showAmharic,
                        bookings = bookings,
                        onDaySelected = { day ->
                            viewModel.setSelectedEthDate(selectedEthDate.copy(day = day))
                        }
                    )
                }
            }
            CalendarViewMode.WEEK -> {
                item {
                    WeekCalendarView(
                        selectedEthDate = selectedEthDate,
                        bookings = bookings,
                        onDaySelected = { day ->
                            viewModel.setSelectedEthDate(selectedEthDate.copy(day = day))
                        }
                    )
                }
            }
            CalendarViewMode.DAY -> {
                item {
                    DayScheduleTimeline(
                        selectedEthDate = selectedEthDate,
                        dayBookings = selectedDayBookings,
                        onBookingClick = { viewModel.inspectBooking(it) }
                    )
                }
            }
            CalendarViewMode.AGENDA -> {
                item {
                    AgendaListView(
                        bookings = bookings,
                        onBookingClick = { viewModel.inspectBooking(it) }
                    )
                }
            }
        }

        // Selected Day Overview Card & Event Listing
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, StudioBorder, RoundedCornerShape(18.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val gregEquiv = EthiopianCalendarHelper.ethToGregorian(selectedEthDate.year, selectedEthDate.month, selectedEthDate.day)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = selectedEthDate.toDisplayString(showAmharic),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioWhite
                            )
                            Text(
                                text = "Gregorian: ${gregEquiv.toDisplayString()}",
                                fontSize = 11.sp,
                                color = StudioTextMuted
                            )
                        }

                        Button(
                            onClick = { viewModel.openAddBooking(selectedEthDate) },
                            colors = ButtonDefaults.buttonColors(containerColor = ErmiOrange),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("book_this_date_button")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Book Date", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = StudioBorder)
                    Spacer(modifier = Modifier.height(10.dp))

                    if (selectedDayBookings.isEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(StudioSurfaceVariant)
                                .padding(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = StatusConfirmed, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "All Production Time Slots Available",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioWhite
                                )
                                Text(
                                    text = "Studio and camera crew are ready for booking on this date.",
                                    fontSize = 11.sp,
                                    color = StudioTextSecondary
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "SCHEDULED SHOOTS (${selectedDayBookings.size})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioGold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        selectedDayBookings.forEach { booking ->
                            BookingCardCompact(
                                booking = booking,
                                onClick = { viewModel.inspectBooking(booking) }
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MonthCalendarGrid(
    selectedEthDate: EthDate,
    daysInMonth: Int,
    firstDayWeekday: Int,
    todayEth: EthDate,
    showAmharic: Boolean,
    bookings: List<BookingEntity>,
    onDaySelected: (Int) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, StudioBorder, RoundedCornerShape(18.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Weekday Headers (Mon-Sun or ሰኞ-እሑድ)
            val weekdayLabels = if (showAmharic) EthiopianCalendarHelper.WEEKDAY_NAMES_AMHARIC else EthiopianCalendarHelper.WEEKDAY_NAMES_SHORT
            Row(modifier = Modifier.fillMaxWidth()) {
                weekdayLabels.forEach { label ->
                    Text(
                        text = label,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Divider(color = StudioBorder.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(6.dp))

            // Calendar Cells Grid: total 35 or 42 slots
            val totalCells = ((firstDayWeekday + daysInMonth + 6) / 7) * 7

            for (weekRow in 0 until (totalCells / 7)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (col in 0..6) {
                        val cellIndex = weekRow * 7 + col
                        val dayNumber = cellIndex - firstDayWeekday + 1

                        if (dayNumber in 1..daysInMonth) {
                            val isSelected = selectedEthDate.day == dayNumber
                            val isToday = todayEth.year == selectedEthDate.year &&
                                    todayEth.month == selectedEthDate.month &&
                                    todayEth.day == dayNumber

                            val dayBookings = bookings.filter {
                                it.ethYear == selectedEthDate.year &&
                                        it.ethMonth == selectedEthDate.month &&
                                        it.ethDay == dayNumber &&
                                        it.bookingStatus != "Cancelled"
                            }

                            val gregEquiv = remember(selectedEthDate.year, selectedEthDate.month, dayNumber) {
                                EthiopianCalendarHelper.ethToGregorian(selectedEthDate.year, selectedEthDate.month, dayNumber)
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        when {
                                            isSelected -> ErmiPurple
                                            isToday -> StudioSurfaceVariant
                                            dayBookings.isNotEmpty() -> StudioCardElevated
                                            else -> Color.Transparent
                                        }
                                    )
                                    .border(
                                        width = when {
                                            isSelected -> 1.5.dp
                                            isToday -> 1.dp
                                            else -> 0.5.dp
                                        },
                                        color = when {
                                            isSelected -> ErmiOrange
                                            isToday -> ErmiOrangeLight.copy(alpha = 0.6f)
                                            else -> StudioBorder.copy(alpha = 0.3f)
                                        },
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { onDaySelected(dayNumber) }
                                    .padding(2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = dayNumber.toString(),
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected || isToday) FontWeight.Black else FontWeight.Bold,
                                        color = when {
                                            isSelected -> StudioWhite
                                            isToday -> ErmiOrange
                                            dayBookings.isNotEmpty() -> StudioWhite
                                            else -> StudioTextSecondary
                                        }
                                    )

                                    // Gregorian Day Subtitle
                                    Text(
                                        text = "${gregEquiv.day}",
                                        fontSize = 8.sp,
                                        color = if (isSelected) StudioWhite.copy(alpha = 0.8f) else StudioTextMuted
                                    )

                                    // Shoot Indicator Dots
                                    if (dayBookings.isNotEmpty()) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                                            modifier = Modifier.padding(top = 1.dp)
                                        ) {
                                            dayBookings.take(3).forEach { b ->
                                                Box(
                                                    modifier = Modifier
                                                        .size(4.dp)
                                                        .clip(CircleShape)
                                                        .background(CategoryColors.get(b.serviceCategory))
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // Blank filler cell
                            Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun WeekCalendarView(
    selectedEthDate: EthDate,
    bookings: List<BookingEntity>,
    onDaySelected: (Int) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth().border(0.5.dp, StudioBorder, RoundedCornerShape(18.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = "7-DAY WEEKLY SCHEDULE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ErmiOrange)
            Spacer(modifier = Modifier.height(10.dp))

            // Show 7 consecutive Ethiopian days centered on selected date
            val daysInMonth = EthiopianCalendarHelper.getDaysInEthMonth(selectedEthDate.year, selectedEthDate.month)
            val startDay = (selectedEthDate.day - 3).coerceIn(1, (daysInMonth - 6).coerceAtLeast(1))

            for (d in startDay until (startDay + 7).coerceAtMost(daysInMonth + 1)) {
                val isSelected = selectedEthDate.day == d
                val dayBookings = bookings.filter {
                    it.ethYear == selectedEthDate.year &&
                            it.ethMonth == selectedEthDate.month &&
                            it.ethDay == d &&
                            it.bookingStatus != "Cancelled"
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) StudioSurfaceVariant else Color.Transparent)
                        .clickable { onDaySelected(d) }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) ErmiOrange else StudioSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = d.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (isSelected) Color.Black else StudioWhite
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Day $d • ${EthiopianCalendarHelper.ETH_MONTH_NAMES[selectedEthDate.month - 1]}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = StudioWhite
                        )
                        Text(
                            text = if (dayBookings.isEmpty()) "Open for booking" else "${dayBookings.size} shoot(s) scheduled",
                            fontSize = 11.sp,
                            color = if (dayBookings.isEmpty()) StatusConfirmed else ErmiOrangeLight
                        )
                    }

                    if (dayBookings.isNotEmpty()) {
                        StatusBadge(status = dayBookings.first().serviceCategory)
                    }
                }
                Divider(color = StudioBorder.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
fun DayScheduleTimeline(
    selectedEthDate: EthDate,
    dayBookings: List<BookingEntity>,
    onBookingClick: (BookingEntity) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth().border(0.5.dp, StudioBorder, RoundedCornerShape(18.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = "DAILY TIMELINE (08:00 AM - 08:00 PM)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ErmiOrange)
            Spacer(modifier = Modifier.height(10.dp))

            val timeSlots = listOf(
                "08:00 AM", "10:00 AM", "12:00 PM", "02:00 PM", "04:00 PM", "06:00 PM"
            )

            timeSlots.forEach { slot ->
                val bookingForSlot = dayBookings.firstOrNull { it.startTime.startsWith(slot.take(2)) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                ) {
                    Text(text = slot, fontSize = 11.sp, color = StudioTextMuted, modifier = Modifier.width(65.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (bookingForSlot != null) ErmiOrange else StudioBorder)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    if (bookingForSlot != null) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ErmiPurpleContainer)
                                .clickable { onBookingClick(bookingForSlot) }
                                .padding(8.dp)
                        ) {
                            Column {
                                Text(text = bookingForSlot.serviceTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                                Text(text = "${bookingForSlot.customerName} • ${bookingForSlot.location}", fontSize = 10.sp, color = StudioTextSecondary)
                            }
                        }
                    } else {
                        Text(text = "Available Slot", fontSize = 11.sp, color = StudioTextMuted)
                    }
                }
                Divider(color = StudioBorder.copy(alpha = 0.2f))
            }
        }
    }
}

@Composable
fun AgendaListView(
    bookings: List<BookingEntity>,
    onBookingClick: (BookingEntity) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth().border(0.5.dp, StudioBorder, RoundedCornerShape(18.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = "ALL SCHEDULED PRODUCTIONS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ErmiOrange)
            Spacer(modifier = Modifier.height(10.dp))

            if (bookings.isEmpty()) {
                Text(text = "No productions in agenda.", fontSize = 12.sp, color = StudioTextMuted)
            } else {
                bookings.forEach { b ->
                    BookingCardCompact(booking = b, onClick = { onBookingClick(b) })
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}
