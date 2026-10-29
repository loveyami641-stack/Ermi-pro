package com.example.ermipro.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.OutlinedButton
import com.example.ermipro.calendar.EthiopianCalendarHelper
import com.example.ermipro.data.model.BookingEntity
import com.example.ermipro.ui.components.BookingCardCompact
import com.example.ermipro.ui.components.CategoryColors
import com.example.ermipro.ui.components.StatusBadge
import com.example.ermipro.ui.viewmodel.ErmiViewModel
import com.example.ermipro.ui.viewmodel.MainTab
import com.example.ui.theme.ErmiOrange
import com.example.ui.theme.ErmiOrangeLight
import com.example.ui.theme.ErmiPurple
import com.example.ui.theme.ErmiPurpleContainer
import com.example.ui.theme.ErmiPurpleLight
import com.example.ui.theme.StatusCancelled
import com.example.ui.theme.StatusCompleted
import com.example.ui.theme.StatusConfirmed
import com.example.ui.theme.StatusInProgress
import com.example.ui.theme.StatusPending
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
fun DashboardScreen(
    viewModel: ErmiViewModel,
    modifier: Modifier = Modifier
) {
    val bookings by viewModel.bookings.collectAsState()
    val customers by viewModel.customers.collectAsState()
    val todayEth = EthiopianCalendarHelper.getTodayEth()
    val todayGreg = EthiopianCalendarHelper.getTodayGreg()
    val context = LocalContext.current

    // Metrics calculations
    val todayBookings = bookings.filter { it.ethYear == todayEth.year && it.ethMonth == todayEth.month && it.ethDay == todayEth.day && it.bookingStatus != "Cancelled" }
    val upcomingBookings = bookings.filter {
        val isFutureYear = it.ethYear > todayEth.year
        val isFutureMonth = it.ethYear == todayEth.year && it.ethMonth > todayEth.month
        val isFutureDay = it.ethYear == todayEth.year && it.ethMonth == todayEth.month && it.ethDay > todayEth.day
        (isFutureYear || isFutureMonth || isFutureDay) && it.bookingStatus != "Cancelled"
    }
    val confirmedCount = bookings.count { it.bookingStatus.equals("Confirmed", ignoreCase = true) }
    val inProgressCount = bookings.count { it.bookingStatus.equals("In Progress", ignoreCase = true) }
    val pendingCount = bookings.count { it.bookingStatus.equals("Pending", ignoreCase = true) || it.bookingStatus.equals("New Request", ignoreCase = true) }
    val completedCount = bookings.count { it.bookingStatus.equals("Completed", ignoreCase = true) }
    val cancelledCount = bookings.count { it.bookingStatus.equals("Cancelled", ignoreCase = true) }

    val totalRevenue = bookings.sumOf { it.paidAmount }
    val outstandingBalance = bookings.sumOf { it.remainingBalance }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground),
        contentPadding = PaddingValues(bottom = 90.dp, top = 8.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Studio Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(ErmiPurpleContainer, Color(0xFF281140), Color(0xFF1F0D33))
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(1.dp, ErmiPurpleLight.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "PRODUCTION DASHBOARD",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = ErmiOrange,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Ermi Video Production",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioWhite
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(StudioSurfaceVariant)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "ETB Currency",
                                fontSize = 10.sp,
                                color = StudioGold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    // Financial summary row
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(text = "Total Collected", fontSize = 11.sp, color = StudioTextSecondary)
                            Text(
                                text = "ETB %,.0f".format(totalRevenue),
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Black,
                                color = StatusConfirmed
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Outstanding Balance", fontSize = 11.sp, color = StudioTextSecondary)
                            Text(
                                text = "ETB %,.0f".format(outstandingBalance),
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Black,
                                color = if (outstandingBalance > 0) ErmiOrange else StatusConfirmed
                            )
                        }
                    }
                }
            }
        }

        // Quick Actions Row
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { viewModel.openAddBooking() },
                    colors = ButtonDefaults.buttonColors(containerColor = ErmiOrange),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).testTag("quick_new_booking_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "New Booking", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Button(
                    onClick = { viewModel.openAddCustomer() },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioSurfaceVariant),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).testTag("quick_add_customer_button")
                ) {
                    Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, tint = StudioWhite, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "+ Customer", color = StudioWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Button(
                    onClick = { viewModel.setTab(MainTab.CALENDAR) },
                    colors = ButtonDefaults.buttonColors(containerColor = ErmiPurple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).testTag("quick_view_calendar_button")
                ) {
                    Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = StudioWhite, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Calendar", color = StudioWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Status Statistics Cards Grid
        item {
            Text(
                text = "OPERATIONAL OVERVIEW",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = StudioTextMuted,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    MetricCard(
                        title = "Today's Shoots",
                        count = todayBookings.size.toString(),
                        subtitle = "${todayEth.toDisplayString()}",
                        indicatorColor = ErmiOrange,
                        modifier = Modifier.weight(1f).clickable { viewModel.setTab(MainTab.BOOKINGS) }
                    )
                    MetricCard(
                        title = "Upcoming",
                        count = upcomingBookings.size.toString(),
                        subtitle = "Scheduled shoots",
                        indicatorColor = StatusInProgress,
                        modifier = Modifier.weight(1f).clickable { viewModel.setTab(MainTab.BOOKINGS) }
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    MetricCard(
                        title = "In Progress",
                        count = inProgressCount.toString(),
                        subtitle = "Active production",
                        indicatorColor = StatusInProgress,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Confirmed",
                        count = confirmedCount.toString(),
                        subtitle = "Secured bookings",
                        indicatorColor = StatusConfirmed,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    MetricCard(
                        title = "Pending Requests",
                        count = pendingCount.toString(),
                        subtitle = "Needs confirmation",
                        indicatorColor = StatusPending,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Total Clients",
                        count = customers.size.toString(),
                        subtitle = "Registered accounts",
                        indicatorColor = ErmiPurpleLight,
                        modifier = Modifier.weight(1f).clickable { viewModel.setTab(MainTab.CUSTOMERS) }
                    )
                }
            }
        }

        // Today's Calendar Schedule Section
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "TODAY'S PRODUCTION SCHEDULE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextMuted,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${todayEth.toDisplayString()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ErmiOrangeLight
                )
            }
        }

        if (todayBookings.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(24.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = StudioTextMuted, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "No production shoots scheduled for today", fontSize = 13.sp, color = StudioTextSecondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { viewModel.openAddBooking(todayEth) },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Book Shoot for Today", fontSize = 12.sp, color = ErmiOrange)
                        }
                    }
                }
            }
        } else {
            items(todayBookings) { booking ->
                BookingCardCompact(
                    booking = booking,
                    onClick = { viewModel.inspectBooking(booking) }
                )
            }
        }

        // Upcoming Production Schedule
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "UPCOMING PROJECTS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextMuted,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "See All (${bookings.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ErmiOrange,
                    modifier = Modifier.clickable { viewModel.setTab(MainTab.BOOKINGS) }
                )
            }
        }

        items(upcomingBookings.take(4)) { booking ->
            BookingCardCompact(
                booking = booking,
                onClick = { viewModel.inspectBooking(booking) }
            )
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    count: String,
    subtitle: String,
    indicatorColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.border(0.5.dp, StudioBorder, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = title, fontSize = 11.sp, color = StudioTextSecondary, fontWeight = FontWeight.Medium)
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(indicatorColor)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = count,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = StudioWhite
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = StudioTextMuted,
                maxLines = 1
            )
        }
    }
}
