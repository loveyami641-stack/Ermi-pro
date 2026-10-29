package com.example.ermipro.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ermipro.calendar.EthDate
import com.example.ermipro.calendar.EthiopianCalendarHelper
import com.example.ermipro.data.model.BookingEntity
import com.example.ermipro.data.model.ServiceEntity
import com.example.ermipro.data.model.StaffEntity
import com.example.ui.theme.ErmiOrange
import com.example.ui.theme.ErmiOrangeLight
import com.example.ui.theme.ErmiPurple
import com.example.ui.theme.ErmiPurpleContainer
import com.example.ui.theme.ErmiPurpleLight
import com.example.ui.theme.StatusCancelled
import com.example.ui.theme.StatusConfirmed
import com.example.ui.theme.StatusInProgress
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
fun BookingDetailDialog(
    booking: BookingEntity,
    onDismiss: () -> Unit,
    onEdit: (BookingEntity) -> Unit,
    onStatusChange: (BookingEntity, String) -> Unit,
    onDelete: (BookingEntity) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = StudioSurface),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 680.dp)
                .border(1.dp, CategoryColors.get(booking.serviceCategory).copy(alpha = 0.5f), RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = booking.bookingNumber,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioGold
                        )
                        Text(
                            text = booking.serviceTitle,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioWhite
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = StudioTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatusBadge(status = booking.bookingStatus)
                    StatusBadge(status = booking.paymentStatus)
                }

                Divider(color = StudioBorder, modifier = Modifier.padding(vertical = 12.dp))

                // Date & Time Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(StudioSurfaceVariant)
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(text = "ETHIOPIAN DATE", fontSize = 10.sp, color = StudioTextMuted, fontWeight = FontWeight.Bold)
                                Text(text = booking.ethDateString, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ErmiOrangeLight)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "GREGORIAN", fontSize = 10.sp, color = StudioTextMuted, fontWeight = FontWeight.Bold)
                                Text(text = booking.gregDateString, fontSize = 12.sp, color = StudioTextSecondary)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Time: ${booking.startTime} - ${booking.endTime}", fontSize = 12.sp, color = StudioWhite, fontWeight = FontWeight.SemiBold)
                            Text(text = booking.serviceCategory, fontSize = 12.sp, color = CategoryColors.get(booking.serviceCategory), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                // Customer Information & Direct Actions
                Text(text = "CUSTOMER & LOCATION", fontSize = 11.sp, color = StudioTextMuted, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = booking.customerName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                Text(text = "Phone: ${booking.customerPhone}", fontSize = 13.sp, color = StudioTextSecondary)
                if (booking.customerEmail.isNotBlank()) {
                    Text(text = "Email: ${booking.customerEmail}", fontSize = 12.sp, color = StudioTextMuted)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = ErmiOrange, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = booking.location, fontSize = 12.sp, color = StudioTextSecondary)
                }

                // Quick Call & WhatsApp Buttons
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${booking.customerPhone.replace(" ", "")}"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StatusConfirmed),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("call_customer_button")
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = Color.Black, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Call Client", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            val cleanPhone = booking.customerPhone.replace("+", "").replace(" ", "")
                            val waUri = Uri.parse("https://wa.me/$cleanPhone")
                            val intent = Intent(Intent.ACTION_VIEW, waUri)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ErmiPurple),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("whatsapp_customer_button")
                    ) {
                        Icon(imageVector = Icons.Default.Message, contentDescription = null, tint = StudioWhite, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "WhatsApp", color = StudioWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                Divider(color = StudioBorder, modifier = Modifier.padding(vertical = 12.dp))

                // Assigned Production Crew & Equipment
                Text(text = "ASSIGNED CREW & EQUIPMENT", fontSize = 11.sp, color = StudioTextMuted, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                if (booking.assignedCameraOperator.isNotBlank()) {
                    Text(text = "• Camera DP: ${booking.assignedCameraOperator}", fontSize = 12.sp, color = StudioWhite)
                }
                if (booking.assignedPhotographer.isNotBlank()) {
                    Text(text = "• Photographer: ${booking.assignedPhotographer}", fontSize = 12.sp, color = StudioWhite)
                }
                if (booking.assignedEditor.isNotBlank()) {
                    Text(text = "• Video Editor: ${booking.assignedEditor}", fontSize = 12.sp, color = StudioWhite)
                }
                if (booking.assignedEquipment.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Gear: ${booking.assignedEquipment}", fontSize = 11.sp, color = ErmiOrangeLight)
                }

                if (booking.projectDescription.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Project Scope: ${booking.projectDescription}", fontSize = 11.sp, color = StudioTextSecondary)
                }

                Divider(color = StudioBorder, modifier = Modifier.padding(vertical = 12.dp))

                // Financial Breakdown
                Text(text = "FINANCIAL SUMMARY", fontSize = 11.sp, color = StudioTextMuted, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Estimated Price:", fontSize = 12.sp, color = StudioTextSecondary)
                    Text(text = "ETB %,.2f".format(booking.estimatedPrice), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Deposit Amount:", fontSize = 12.sp, color = StudioTextSecondary)
                    Text(text = "ETB %,.2f".format(booking.depositAmount), fontSize = 12.sp, color = StudioGold)
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Total Paid:", fontSize = 12.sp, color = StudioTextSecondary)
                    Text(text = "ETB %,.2f".format(booking.paidAmount), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StatusConfirmed)
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Remaining Balance:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                    Text(
                        text = "ETB %,.2f".format(booking.remainingBalance),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = if (booking.remainingBalance > 0) ErmiOrange else StatusConfirmed
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                // Quick Status Change Options
                Text(text = "UPDATE STATUS", fontSize = 10.sp, color = StudioTextMuted, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                    listOf("Confirmed", "In Progress", "Completed", "Cancelled").forEach { statusOption ->
                        val isSelected = booking.bookingStatus == statusOption
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) ErmiOrange else StudioSurfaceVariant)
                                .clickable { onStatusChange(booking, statusOption) }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = statusOption,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.Black else StudioWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                // Footer Actions
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { onEdit(booking) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("edit_booking_button")
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Edit", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    """
                                    ERMI VIDEO PRODUCTION - BOOKING SHEET
                                    Booking Ref: ${booking.bookingNumber}
                                    Customer: ${booking.customerName} (${booking.customerPhone})
                                    Service: ${booking.serviceTitle}
                                    Ethiopian Date: ${booking.ethDateString}
                                    Time: ${booking.startTime} - ${booking.endTime}
                                    Location: ${booking.location}
                                    Camera DP: ${booking.assignedCameraOperator}
                                    Photographer: ${booking.assignedPhotographer}
                                    Editor: ${booking.assignedEditor}
                                    Status: ${booking.bookingStatus}
                                    Remaining Balance: ETB %,.2f
                                    Office: Eldana Mall 2nd Floor, Hosaena
                                    Contact: +251 917 800 851
                                    """.trimIndent().format(booking.remainingBalance)
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Booking Sheet"))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ErmiOrange),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("share_booking_button")
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Share Slip", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    IconButton(
                        onClick = { onDelete(booking) },
                        modifier = Modifier.testTag("delete_booking_button")
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = StatusCancelled)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBookingDialog(
    initialBooking: BookingEntity?,
    presetEthDate: EthDate?,
    services: List<ServiceEntity>,
    staffList: List<StaffEntity>,
    onDismiss: () -> Unit,
    onSave: (BookingEntity) -> Unit,
    checkConflict: (Int, Int, Int, String, String, Long) -> com.example.ermipro.ui.viewmodel.ConflictWarning
) {
    val isEdit = initialBooking != null

    val defaultEthDate = initialBooking?.let { EthDate(it.ethYear, it.ethMonth, it.ethDay) }
        ?: presetEthDate
        ?: EthiopianCalendarHelper.getTodayEth()

    var customerName by remember { mutableStateOf(initialBooking?.customerName ?: "") }
    var customerPhone by remember { mutableStateOf(initialBooking?.customerPhone ?: "") }
    var customerEmail by remember { mutableStateOf(initialBooking?.customerEmail ?: "") }
    var serviceCategory by remember { mutableStateOf(initialBooking?.serviceCategory ?: "Music Video") }
    var serviceTitle by remember { mutableStateOf(initialBooking?.serviceTitle ?: "Music Video Production") }

    var ethYear by remember { mutableStateOf(defaultEthDate.year) }
    var ethMonth by remember { mutableStateOf(defaultEthDate.month) }
    var ethDay by remember { mutableStateOf(defaultEthDate.day) }

    // Live Gregorian date calculation
    val gregDate = remember(ethYear, ethMonth, ethDay) {
        EthiopianCalendarHelper.ethToGregorian(ethYear, ethMonth, ethDay)
    }

    var startTime by remember { mutableStateOf(initialBooking?.startTime ?: "09:00 AM") }
    var endTime by remember { mutableStateOf(initialBooking?.endTime ?: "02:00 PM") }
    var location by remember { mutableStateOf(initialBooking?.location ?: "Eldana Mall 2nd Floor Studio, Hosaena") }

    var assignedCameraOperator by remember { mutableStateOf(initialBooking?.assignedCameraOperator ?: "Ermias Tadesse") }
    var assignedPhotographer by remember { mutableStateOf(initialBooking?.assignedPhotographer ?: "Selamawit Abebe") }
    var assignedEditor by remember { mutableStateOf(initialBooking?.assignedEditor ?: "Biruk Mengistu") }
    var assignedEquipment by remember { mutableStateOf(initialBooking?.assignedEquipment ?: "Sony FX3 Cinema Line, DJI RS3 Pro") }

    var projectDescription by remember { mutableStateOf(initialBooking?.projectDescription ?: "") }
    var estimatedPriceStr by remember { mutableStateOf(initialBooking?.estimatedPrice?.toInt()?.toString() ?: "45000") }
    var depositAmountStr by remember { mutableStateOf(initialBooking?.depositAmount?.toInt()?.toString() ?: "20000") }
    var paidAmountStr by remember { mutableStateOf(initialBooking?.paidAmount?.toInt()?.toString() ?: "20000") }
    var bookingStatus by remember { mutableStateOf(initialBooking?.bookingStatus ?: "Confirmed") }

    var overrideConflict by remember { mutableStateOf(false) }

    // Live conflict check
    val conflict = remember(ethYear, ethMonth, ethDay, assignedCameraOperator, assignedEquipment) {
        checkConflict(
            ethYear,
            ethMonth,
            ethDay,
            assignedCameraOperator,
            assignedEquipment,
            initialBooking?.id ?: 0L
        )
    }

    var serviceDropdownExpanded by remember { mutableStateOf(false) }
    var statusDropdownExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = StudioSurface),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 700.dp)
                .border(1.dp, ErmiPurpleLight.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isEdit) "Edit Booking" else "New Production Booking",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioWhite
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = StudioTextMuted)
                    }
                }

                // Conflict Banner Alert (Critical requirement)
                if (conflict.hasConflict) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(StatusCancelled.copy(alpha = 0.15f))
                            .border(1.dp, StatusCancelled, RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = StatusCancelled, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "DOUBLE-BOOKING WARNING",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusCancelled
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "'${conflict.conflictingEntityName}' is already assigned on this Ethiopian date to Booking #${conflict.conflictingBooking?.bookingNumber} (${conflict.conflictingBooking?.customerName}).",
                                fontSize = 11.sp,
                                color = StudioWhite
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Checkbox(
                                    checked = overrideConflict,
                                    onCheckedChange = { overrideConflict = it },
                                    colors = CheckboxDefaults.colors(checkedColor = ErmiOrange)
                                )
                                Text(
                                    text = "Authorized Override Double-Booking",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ErmiOrange
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                // Customer Fields
                Text(text = "CUSTOMER DETAILS", fontSize = 11.sp, color = StudioTextMuted, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Customer Full Name *") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = StudioWhite,
                        unfocusedTextColor = StudioWhite,
                        focusedContainerColor = StudioSurfaceVariant,
                        unfocusedContainerColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("booking_customer_name_input")
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = { customerPhone = it },
                        label = { Text("Phone Number *") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = StudioWhite,
                            unfocusedTextColor = StudioWhite,
                            focusedContainerColor = StudioSurfaceVariant,
                            unfocusedContainerColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f).testTag("booking_customer_phone_input")
                    )
                    OutlinedTextField(
                        value = customerEmail,
                        onValueChange = { customerEmail = it },
                        label = { Text("Email (Optional)") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = StudioWhite,
                            unfocusedTextColor = StudioWhite,
                            focusedContainerColor = StudioSurfaceVariant,
                            unfocusedContainerColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                // Service Selector
                Text(text = "SERVICE & PRODUCTION TYPE", fontSize = 11.sp, color = StudioTextMuted, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                ExposedDropdownMenuBox(
                    expanded = serviceDropdownExpanded,
                    onExpandedChange = { serviceDropdownExpanded = !serviceDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = serviceTitle,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Production Service") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = serviceDropdownExpanded) },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = StudioWhite,
                            unfocusedTextColor = StudioWhite,
                            focusedContainerColor = StudioSurfaceVariant,
                            unfocusedContainerColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = serviceDropdownExpanded,
                        onDismissRequest = { serviceDropdownExpanded = false },
                        modifier = Modifier.background(StudioCardElevated)
                    ) {
                        services.forEach { s ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(text = s.title, fontWeight = FontWeight.Bold, color = StudioWhite)
                                        Text(text = "ETB %,.0f • ${s.category}".format(s.basePrice), fontSize = 11.sp, color = ErmiOrangeLight)
                                    }
                                },
                                onClick = {
                                    serviceTitle = s.title
                                    serviceCategory = s.category
                                    estimatedPriceStr = s.basePrice.toInt().toString()
                                    serviceDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                // Ethiopian Calendar Booking Date
                Text(text = "ETHIOPIAN CALENDAR DATE", fontSize = 11.sp, color = ErmiOrange, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    // Month Picker
                    Box(modifier = Modifier.weight(1.4f)) {
                        var monthMenu by remember { mutableStateOf(false) }
                        OutlinedTextField(
                            value = EthiopianCalendarHelper.ETH_MONTH_NAMES.getOrElse(ethMonth - 1) { "Month $ethMonth" },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Eth Month") },
                            trailingIcon = {
                                IconButton(onClick = { monthMenu = true }) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = StudioWhite,
                                unfocusedTextColor = StudioWhite,
                                focusedContainerColor = StudioSurfaceVariant,
                                unfocusedContainerColor = StudioSurfaceVariant
                            ),
                            modifier = Modifier.fillMaxWidth().clickable { monthMenu = true }
                        )
                        DropdownMenu(
                            expanded = monthMenu,
                            onDismissRequest = { monthMenu = false },
                            modifier = Modifier.background(StudioCardElevated)
                        ) {
                            EthiopianCalendarHelper.ETH_MONTH_NAMES.forEachIndexed { index, mName ->
                                DropdownMenuItem(
                                    text = { Text(text = "${index + 1}. $mName", color = StudioWhite) },
                                    onClick = {
                                        ethMonth = index + 1
                                        val maxDays = EthiopianCalendarHelper.getDaysInEthMonth(ethYear, ethMonth)
                                        if (ethDay > maxDays) ethDay = maxDays
                                        monthMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Day
                    OutlinedTextField(
                        value = ethDay.toString(),
                        onValueChange = {
                            val max = EthiopianCalendarHelper.getDaysInEthMonth(ethYear, ethMonth)
                            val num = it.toIntOrNull()
                            if (num != null && num in 1..max) ethDay = num
                        },
                        label = { Text("Day (1-30)") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = StudioWhite,
                            unfocusedTextColor = StudioWhite,
                            focusedContainerColor = StudioSurfaceVariant,
                            unfocusedContainerColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.weight(0.9f)
                    )

                    // Year
                    OutlinedTextField(
                        value = ethYear.toString(),
                        onValueChange = {
                            val num = it.toIntOrNull()
                            if (num != null && num in 2010..2040) ethYear = num
                        },
                        label = { Text("Eth Year") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = StudioWhite,
                            unfocusedTextColor = StudioWhite,
                            focusedContainerColor = StudioSurfaceVariant,
                            unfocusedContainerColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Equivalent Gregorian Display
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Gregorian Equivalent: ${gregDate.toDisplayString()}",
                    fontSize = 11.sp,
                    color = StudioTextMuted
                )

                Spacer(modifier = Modifier.height(10.dp))
                // Time & Location
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { startTime = it },
                        label = { Text("Start Time") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = StudioWhite,
                            unfocusedTextColor = StudioWhite,
                            focusedContainerColor = StudioSurfaceVariant,
                            unfocusedContainerColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { endTime = it },
                        label = { Text("End Time") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = StudioWhite,
                            unfocusedTextColor = StudioWhite,
                            focusedContainerColor = StudioSurfaceVariant,
                            unfocusedContainerColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Shoot Location (e.g. Eldana Mall Studio, Hosaena)") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = StudioWhite,
                        unfocusedTextColor = StudioWhite,
                        focusedContainerColor = StudioSurfaceVariant,
                        unfocusedContainerColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                // Crew Assignment
                Text(text = "PRODUCTION TEAM ASSIGNMENT", fontSize = 11.sp, color = StudioTextMuted, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = assignedCameraOperator,
                    onValueChange = { assignedCameraOperator = it },
                    label = { Text("Camera DP (e.g. Ermias Tadesse)") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = StudioWhite,
                        unfocusedTextColor = StudioWhite,
                        focusedContainerColor = StudioSurfaceVariant,
                        unfocusedContainerColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = assignedPhotographer,
                        onValueChange = { assignedPhotographer = it },
                        label = { Text("Photographer") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = StudioWhite,
                            unfocusedTextColor = StudioWhite,
                            focusedContainerColor = StudioSurfaceVariant,
                            unfocusedContainerColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = assignedEditor,
                        onValueChange = { assignedEditor = it },
                        label = { Text("Video Editor") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = StudioWhite,
                            unfocusedTextColor = StudioWhite,
                            focusedContainerColor = StudioSurfaceVariant,
                            unfocusedContainerColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = assignedEquipment,
                    onValueChange = { assignedEquipment = it },
                    label = { Text("Assigned Equipment") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = StudioWhite,
                        unfocusedTextColor = StudioWhite,
                        focusedContainerColor = StudioSurfaceVariant,
                        unfocusedContainerColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                // Pricing & Payments
                Text(text = "PRICING & PAYMENTS (ETB)", fontSize = 11.sp, color = StudioTextMuted, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = estimatedPriceStr,
                        onValueChange = { estimatedPriceStr = it },
                        label = { Text("Total Price") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = StudioWhite,
                            unfocusedTextColor = StudioWhite,
                            focusedContainerColor = StudioSurfaceVariant,
                            unfocusedContainerColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = paidAmountStr,
                        onValueChange = { paidAmountStr = it },
                        label = { Text("Amount Paid") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = StudioWhite,
                            unfocusedTextColor = StudioWhite,
                            focusedContainerColor = StudioSurfaceVariant,
                            unfocusedContainerColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                // Status selector
                ExposedDropdownMenuBox(
                    expanded = statusDropdownExpanded,
                    onExpandedChange = { statusDropdownExpanded = !statusDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = bookingStatus,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Booking Status") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusDropdownExpanded) },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = StudioWhite,
                            unfocusedTextColor = StudioWhite,
                            focusedContainerColor = StudioSurfaceVariant,
                            unfocusedContainerColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = statusDropdownExpanded,
                        onDismissRequest = { statusDropdownExpanded = false },
                        modifier = Modifier.background(StudioCardElevated)
                    ) {
                        listOf("New Request", "Pending", "Confirmed", "In Progress", "Completed", "Cancelled").forEach { status ->
                            DropdownMenuItem(
                                text = { Text(text = status, color = StudioWhite) },
                                onClick = {
                                    bookingStatus = status
                                    statusDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                // Save Button
                val canSubmit = customerName.isNotBlank() && customerPhone.isNotBlank() && (!conflict.hasConflict || overrideConflict)

                Button(
                    onClick = {
                        val price = estimatedPriceStr.toDoubleOrNull() ?: 0.0
                        val paid = paidAmountStr.toDoubleOrNull() ?: 0.0
                        val deposit = depositAmountStr.toDoubleOrNull() ?: (price * 0.5)
                        val paymentStatus = when {
                            paid >= price && price > 0 -> "Fully Paid"
                            paid > 0 -> "Partially Paid"
                            else -> "Unpaid"
                        }
                        val bookingNumber = initialBooking?.bookingNumber
                            ?: "EVP-$ethYear-%03d".format((100..999).random())

                        val entity = BookingEntity(
                            id = initialBooking?.id ?: 0L,
                            bookingNumber = bookingNumber,
                            customerName = customerName.trim(),
                            customerPhone = customerPhone.trim(),
                            customerEmail = customerEmail.trim(),
                            serviceCategory = serviceCategory,
                            serviceTitle = serviceTitle,
                            ethYear = ethYear,
                            ethMonth = ethMonth,
                            ethDay = ethDay,
                            gregYear = gregDate.year,
                            gregMonth = gregDate.month,
                            gregDay = gregDate.day,
                            startTime = startTime,
                            endTime = endTime,
                            location = location.trim(),
                            requiredService = serviceTitle,
                            additionalServices = "",
                            assignedCameraOperator = assignedCameraOperator.trim(),
                            assignedPhotographer = assignedPhotographer.trim(),
                            assignedEditor = assignedEditor.trim(),
                            assignedEquipment = assignedEquipment.trim(),
                            projectDescription = projectDescription.trim(),
                            estimatedPrice = price,
                            depositAmount = deposit,
                            paidAmount = paid,
                            bookingStatus = bookingStatus,
                            paymentStatus = paymentStatus,
                            notes = ""
                        )
                        onSave(entity)
                    },
                    enabled = canSubmit,
                    colors = ButtonDefaults.buttonColors(containerColor = ErmiOrange),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("save_booking_submit_button")
                ) {
                    Text(
                        text = if (isEdit) "Update Booking" else "Confirm & Save Booking",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
