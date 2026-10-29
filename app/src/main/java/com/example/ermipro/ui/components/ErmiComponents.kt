package com.example.ermipro.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.ermipro.calendar.EthiopianCalendarHelper
import com.example.ermipro.data.model.BookingEntity
import com.example.ermipro.data.model.PaymentEntity
import com.example.ermipro.ui.viewmodel.UserRole
import com.example.ui.theme.CatBirthday
import com.example.ui.theme.CatCommercial
import com.example.ui.theme.CatDocumentary
import com.example.ui.theme.CatGraduation
import com.example.ui.theme.CatMusicVideo
import com.example.ui.theme.CatPortrait
import com.example.ui.theme.CatStudio
import com.example.ui.theme.CatWedding
import com.example.ui.theme.ErmiOrange
import com.example.ui.theme.ErmiOrangeLight
import com.example.ui.theme.ErmiPurple
import com.example.ui.theme.ErmiPurpleLight
import com.example.ui.theme.ErmiPurpleContainer
import com.example.ui.theme.StatusCancelled
import com.example.ui.theme.StatusCompleted
import com.example.ui.theme.StatusConfirmed
import com.example.ui.theme.StatusInProgress
import com.example.ui.theme.StatusNew
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioCardElevated
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioGold
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextSecondary
import com.example.ui.theme.StudioWhite

object CategoryColors {
    fun get(category: String): Color {
        return when {
            category.contains("Music", ignoreCase = true) -> CatMusicVideo
            category.contains("Commercial", ignoreCase = true) || category.contains("TV", ignoreCase = true) -> CatCommercial
            category.contains("Documentary", ignoreCase = true) -> CatDocumentary
            category.contains("Wedding", ignoreCase = true) -> CatWedding
            category.contains("Birthday", ignoreCase = true) -> CatBirthday
            category.contains("Graduation", ignoreCase = true) -> CatGraduation
            category.contains("Portrait", ignoreCase = true) -> CatPortrait
            category.contains("Studio", ignoreCase = true) -> CatStudio
            else -> ErmiOrange
        }
    }
}

@Composable
fun StatusBadge(status: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (status) {
        "Confirmed" -> StatusConfirmed.copy(alpha = 0.2f) to StatusConfirmed
        "In Progress" -> StatusInProgress.copy(alpha = 0.2f) to StatusInProgress
        "Pending" -> StatusPending.copy(alpha = 0.2f) to StatusPending
        "New Request" -> StatusNew.copy(alpha = 0.2f) to StatusNew
        "Completed" -> StatusCompleted.copy(alpha = 0.2f) to StatusCompleted
        "Cancelled" -> StatusCancelled.copy(alpha = 0.2f) to StatusCancelled
        "Fully Paid" -> StatusConfirmed.copy(alpha = 0.2f) to StatusConfirmed
        "Partially Paid" -> StatusPending.copy(alpha = 0.2f) to StatusPending
        "Unpaid" -> StatusCancelled.copy(alpha = 0.2f) to StatusCancelled
        else -> StudioSurfaceVariant to StudioWhite
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(0.5.dp, textColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = status,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ErmiTopBar(
    activeRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    todayEthDateString: String,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Surface(
        color = StudioSurface,
        shadowElevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Ermi Production Brand Logo and Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        // Quick company info dial
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+251917800851"))
                        context.startActivity(dialIntent)
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ermi_logo),
                            contentDescription = "Ermi Video Production Logo",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ERMI",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = StudioWhite,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "PRO",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = ErmiOrange,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = "Hosaena • Eldana Mall",
                            fontSize = 11.sp,
                            color = StudioTextMuted
                        )
                    }
                }

                // Active Role Pill & Selector
                Box {
                    Surface(
                        color = ErmiPurpleContainer,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { menuExpanded = true }
                            .border(1.dp, ErmiPurpleLight.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(ErmiOrange)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = activeRole.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioWhite
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier.background(StudioCardElevated)
                    ) {
                        UserRole.values().forEach { role ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = role.label,
                                            color = if (role == activeRole) ErmiOrange else StudioWhite,
                                            fontWeight = if (role == activeRole) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                },
                                onClick = {
                                    onRoleSelected(role)
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Sub-header with today's Ethiopian Date
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = ErmiOrange,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Today: $todayEthDateString",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = StudioTextSecondary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+251910040877"))
                        context.startActivity(callIntent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call studio",
                        tint = StatusConfirmed,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "0917 800 851",
                        fontSize = 11.sp,
                        color = StatusConfirmed,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun OfficialReceiptDialog(
    receipt: PaymentEntity,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = StudioSurface),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ErmiPurpleLight.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Receipt Header with Company Branding
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ermi_logo),
                                contentDescription = "Ermi Logo",
                                modifier = Modifier.size(38.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ERMI PRODUCTION",
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp,
                                color = StudioWhite
                            )
                            Text(
                                text = "Official Payment Receipt",
                                fontSize = 11.sp,
                                color = ErmiOrange,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = StudioTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Eldana Mall, 2nd Fl, Office 17, Hosaena • Behind Nib Bank\nTel: +251 917 800 851 / +251 910 040 877",
                    fontSize = 10.sp,
                    color = StudioTextMuted,
                    lineHeight = 14.sp
                )

                Divider(
                    color = StudioBorder,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                // Receipt Metadata
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(text = "RECEIPT NO", fontSize = 10.sp, color = StudioTextMuted, fontWeight = FontWeight.Bold)
                        Text(text = receipt.receiptNumber, fontSize = 13.sp, color = StudioGold, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "DATE (ETHIOPIAN)", fontSize = 10.sp, color = StudioTextMuted, fontWeight = FontWeight.Bold)
                        Text(text = receipt.ethDate, fontSize = 12.sp, color = StudioWhite, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(text = "RECEIVED FROM", fontSize = 10.sp, color = StudioTextMuted, fontWeight = FontWeight.Bold)
                        Text(text = receipt.customerName, fontSize = 14.sp, color = StudioWhite, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "PAYMENT METHOD", fontSize = 10.sp, color = StudioTextMuted, fontWeight = FontWeight.Bold)
                        Text(text = receipt.paymentMethod, fontSize = 12.sp, color = ErmiOrangeLight, fontWeight = FontWeight.SemiBold)
                    }
                }

                if (receipt.referenceNumber.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "TXN REF: ${receipt.referenceNumber}", fontSize = 11.sp, color = StudioCyan)
                }

                Spacer(modifier = Modifier.height(14.dp))
                // Big Amount Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ErmiPurpleContainer)
                        .border(1.dp, ErmiPurpleLight.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "AMOUNT PAID", fontSize = 11.sp, color = StudioTextSecondary, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = "ETB %,.2f".format(receipt.amount),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = StudioWhite
                        )
                        Text(text = "Ethiopian Birr", fontSize = 10.sp, color = ErmiOrange)
                    }
                }

                if (receipt.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Notes: ${receipt.notes}", fontSize = 11.sp, color = StudioTextMuted)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(text = "Authorized Signature", fontSize = 10.sp, color = StudioTextMuted)
                        Text(text = "Ermias T. (Director)", fontSize = 11.sp, color = StudioWhite, fontWeight = FontWeight.SemiBold)
                    }
                    StatusBadge(status = receipt.paymentStatus)
                }

                Spacer(modifier = Modifier.height(20.dp))
                // Share / Print Button
                Button(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                """
                                ERMI VIDEO PRODUCTION - RECEIPT
                                Receipt No: ${receipt.receiptNumber}
                                Customer: ${receipt.customerName}
                                Ethiopian Date: ${receipt.ethDate}
                                Amount Paid: ETB %,.2f
                                Payment Method: ${receipt.paymentMethod}
                                Location: Eldana Mall 2nd Floor, Hosaena
                                Tel: +251 917 800 851 / +251 910 040 877
                                """.trimIndent().format(receipt.amount)
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Official Receipt"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErmiOrange),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("share_receipt_button")
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Share / Print Receipt", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun BookingCardCompact(
    booking: BookingEntity,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(0.5.dp, StudioBorder, RoundedCornerShape(14.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(CategoryColors.get(booking.serviceCategory))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = booking.serviceTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioWhite,
                        maxLines = 1
                    )
                    StatusBadge(status = booking.bookingStatus)
                }

                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${booking.customerName} • ${booking.startTime}",
                    fontSize = 12.sp,
                    color = StudioTextSecondary,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(3.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = booking.ethDateString,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ErmiOrangeLight
                    )
                    Text(
                        text = "ETB %,.0f".format(booking.estimatedPrice),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioGold
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = StudioTextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

