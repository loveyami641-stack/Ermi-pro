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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ermipro.data.model.BookingEntity
import com.example.ermipro.ui.components.CategoryColors
import com.example.ermipro.ui.components.StatusBadge
import com.example.ermipro.ui.viewmodel.ErmiViewModel
import com.example.ui.theme.ErmiOrange
import com.example.ui.theme.ErmiOrangeLight
import com.example.ui.theme.ErmiPurple
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
fun BookingsScreen(
    viewModel: ErmiViewModel,
    modifier: Modifier = Modifier
) {
    val filteredBookings by viewModel.filteredBookings.collectAsState()
    val searchQuery by viewModel.bookingSearchQuery.collectAsState()
    val statusFilter by viewModel.bookingStatusFilter.collectAsState()
    val context = LocalContext.current

    val filterOptions = listOf("All", "Today", "Upcoming", "Confirmed", "In Progress", "Pending", "Completed", "Cancelled")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = StudioBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddBooking() },
                containerColor = ErmiOrange,
                contentColor = Color.Black,
                modifier = Modifier.padding(bottom = 80.dp).testTag("fab_new_booking")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "New Booking")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Input
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setBookingSearchQuery(it) },
                    placeholder = { Text("Search customer, phone, booking ID, service...", fontSize = 12.sp) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = StudioTextMuted) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setBookingSearchQuery("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = StudioTextMuted)
                            }
                        }
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = StudioWhite,
                        unfocusedTextColor = StudioWhite,
                        focusedContainerColor = StudioSurface,
                        unfocusedContainerColor = StudioSurface
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("booking_search_input")
                )
            }

            // Quick Filter Chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filterOptions) { filter ->
                    val isSelected = statusFilter.equals(filter, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) ErmiOrange else StudioSurfaceVariant)
                            .border(
                                0.5.dp,
                                if (isSelected) ErmiOrange else StudioBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { viewModel.setBookingStatusFilter(filter) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = filter,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) Color.Black else StudioWhite
                        )
                    }
                }
            }

            // Bookings List
            if (filteredBookings.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Videocam, contentDescription = null, tint = StudioTextMuted, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No bookings found",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioWhite
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try adjusting your search query or status filter.",
                            fontSize = 12.sp,
                            color = StudioTextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 95.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredBookings, key = { it.id }) { booking ->
                        BookingDetailedCard(
                            booking = booking,
                            onClick = { viewModel.inspectBooking(booking) },
                            onCall = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${booking.customerPhone.replace(" ", "")}"))
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookingDetailedCard(
    booking: BookingEntity,
    onClick: () -> Unit,
    onCall: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(0.5.dp, StudioBorder, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row: Booking ID, Category Pill, Status Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(CategoryColors.get(booking.serviceCategory))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = booking.bookingNumber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioGold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${booking.serviceCategory}",
                        fontSize = 11.sp,
                        color = StudioTextMuted
                    )
                }

                StatusBadge(status = booking.bookingStatus)
            }

            Spacer(modifier = Modifier.height(6.dp))
            // Title & Customer Name
            Text(
                text = booking.serviceTitle,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = StudioWhite
            )

            Spacer(modifier = Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${booking.customerName} (${booking.customerPhone})",
                    fontSize = 13.sp,
                    color = StudioTextSecondary
                )
                IconButton(
                    onClick = onCall,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = "Call", tint = StatusConfirmed, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            // Ethiopian Date & Time & Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${booking.ethDateString} • ${booking.startTime} - ${booking.endTime}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ErmiOrangeLight
                )
            }

            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = StudioTextMuted, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = booking.location,
                    fontSize = 11.sp,
                    color = StudioTextMuted,
                    maxLines = 1
                )
            }

            Divider(color = StudioBorder.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 8.dp))

            // Footer: Assigned Crew & Financial Balance
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "DP: ${booking.assignedCameraOperator.ifEmpty { "Unassigned" }}",
                        fontSize = 10.sp,
                        color = StudioTextSecondary
                    )
                    StatusBadge(status = booking.paymentStatus)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total: ETB %,.0f".format(booking.estimatedPrice),
                        fontSize = 11.sp,
                        color = StudioTextSecondary
                    )
                    Text(
                        text = "Balance: ETB %,.0f".format(booking.remainingBalance),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (booking.remainingBalance > 0) ErmiOrange else StatusConfirmed
                    )
                }
            }
        }
    }
}
