package com.example.ermipro.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.ermipro.data.model.EquipmentEntity
import com.example.ermipro.data.model.PaymentEntity
import com.example.ermipro.data.model.ServiceEntity
import com.example.ermipro.data.model.StaffEntity
import com.example.ermipro.ui.components.CategoryColors
import com.example.ermipro.ui.components.StatusBadge
import com.example.ermipro.ui.viewmodel.ErmiViewModel
import com.example.ermipro.ui.viewmodel.MoreSubView
import com.example.ermipro.ui.viewmodel.UserRole
import com.example.ui.theme.ErmiOrange
import com.example.ui.theme.ErmiOrangeLight
import com.example.ui.theme.ErmiPurple
import com.example.ui.theme.ErmiPurpleContainer
import com.example.ui.theme.ErmiPurpleLight
import com.example.ui.theme.StatusCancelled
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
fun MoreScreen(
    viewModel: ErmiViewModel,
    modifier: Modifier = Modifier
) {
    val currentSubView by viewModel.currentMoreSubView.collectAsState()

    // Handle back button for sub-views as required by navigation guidelines
    BackHandler(enabled = currentSubView != MoreSubView.NONE) {
        viewModel.setMoreSubView(MoreSubView.NONE)
    }

    when (currentSubView) {
        MoreSubView.NONE -> MoreMainMenu(viewModel, modifier)
        MoreSubView.SERVICES -> ServicesSubScreen(viewModel, modifier)
        MoreSubView.PAYMENTS -> PaymentsSubScreen(viewModel, modifier)
        MoreSubView.STAFF -> StaffSubScreen(viewModel, modifier)
        MoreSubView.EQUIPMENT -> EquipmentSubScreen(viewModel, modifier)
        MoreSubView.REPORTS -> ReportsSubScreen(viewModel, modifier)
        MoreSubView.SETTINGS -> SettingsSubScreen(viewModel, modifier)
    }
}

@Composable
fun MoreMainMenu(
    viewModel: ErmiViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground),
        contentPadding = PaddingValues(bottom = 95.dp, top = 8.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Company Brand Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ErmiPurpleLight.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ermi_logo),
                            contentDescription = "Ermi Logo",
                            modifier = Modifier.size(46.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ERMI PRODUCTION",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = StudioWhite
                        )
                        Text(
                            text = "Hosaena City • Eldana Mall 2nd Fl.",
                            fontSize = 11.sp,
                            color = ErmiOrangeLight
                        )
                        Text(
                            text = "+251 917 800 851 • +251 910 040 877",
                            fontSize = 11.sp,
                            color = StudioTextSecondary
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "BUSINESS MANAGEMENT MODULES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = StudioTextMuted,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            MoreMenuCard(
                icon = Icons.Default.Movie,
                title = "Production Services",
                subtitle = "Music videos, TV ads, weddings, documentaries",
                badge = "8 Services",
                onClick = { viewModel.setMoreSubView(MoreSubView.SERVICES) }
            )
        }

        item {
            MoreMenuCard(
                icon = Icons.Default.Receipt,
                title = "Payments & Receipts",
                subtitle = "Payment records, official receipts, balances",
                badge = "ETB Receipts",
                onClick = { viewModel.setMoreSubView(MoreSubView.PAYMENTS) }
            )
        }

        item {
            MoreMenuCard(
                icon = Icons.Default.People,
                title = "Staff & Crew Roster",
                subtitle = "Camera operators, editors, photographers, crew",
                badge = "Team",
                onClick = { viewModel.setMoreSubView(MoreSubView.STAFF) }
            )
        }

        item {
            MoreMenuCard(
                icon = Icons.Default.CameraAlt,
                title = "Equipment Inventory",
                subtitle = "Cameras, lenses, gimbals, lighting, audio",
                badge = "Gear",
                onClick = { viewModel.setMoreSubView(MoreSubView.EQUIPMENT) }
            )
        }

        item {
            MoreMenuCard(
                icon = Icons.Default.Analytics,
                title = "Reports & Analytics",
                subtitle = "Revenue, service popularity, project statistics",
                badge = "Analytics",
                onClick = { viewModel.setMoreSubView(MoreSubView.REPORTS) }
            )
        }

        item {
            MoreMenuCard(
                icon = Icons.Default.Settings,
                title = "Application Settings",
                subtitle = "User roles, company profile, calendar toggle",
                badge = "Config",
                onClick = { viewModel.setMoreSubView(MoreSubView.SETTINGS) }
            )
        }
    }
}

@Composable
fun MoreMenuCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    badge: String,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(0.5.dp, StudioBorder, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ErmiPurpleContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = ErmiOrange, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                Text(text = subtitle, fontSize = 11.sp, color = StudioTextSecondary, maxLines = 1)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(StudioSurfaceVariant)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = badge, fontSize = 10.sp, color = ErmiOrangeLight, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = StudioTextMuted, modifier = Modifier.size(18.dp))
        }
    }
}

// 1. SERVICES SUB-SCREEN
@Composable
fun ServicesSubScreen(viewModel: ErmiViewModel, modifier: Modifier) {
    val services by viewModel.services.collectAsState()
    var isAddOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        SubScreenHeader(
            title = "Production Services",
            onBack = { viewModel.setMoreSubView(MoreSubView.NONE) },
            actionIcon = Icons.Default.Add,
            onAction = { isAddOpen = true }
        )

        LazyColumn(
            contentPadding = PaddingValues(bottom = 95.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(services, key = { it.id }) { service ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().border(0.5.dp, StudioBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = service.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                            Text(
                                text = "ETB %,.0f".format(service.basePrice),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioGold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = service.description, fontSize = 12.sp, color = StudioTextSecondary)
                        if (service.subServices.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "Includes: ${service.subServices}", fontSize = 11.sp, color = ErmiOrangeLight)
                        }
                    }
                }
            }
        }
    }

    if (isAddOpen) {
        var title by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Other") }
        var description by remember { mutableStateOf("") }
        var priceStr by remember { mutableStateOf("25000") }
        var subServices by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { isAddOpen = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, ErmiPurpleLight.copy(alpha = 0.5f), RoundedCornerShape(20.dp)).padding(4.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = "Add Custom Service", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Service Title *") },
                        colors = TextFieldDefaults.colors(focusedTextColor = StudioWhite, unfocusedTextColor = StudioWhite, focusedContainerColor = StudioSurfaceVariant, unfocusedContainerColor = StudioSurfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        label = { Text("Base Price (ETB)") },
                        colors = TextFieldDefaults.colors(focusedTextColor = StudioWhite, unfocusedTextColor = StudioWhite, focusedContainerColor = StudioSurfaceVariant, unfocusedContainerColor = StudioSurfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        colors = TextFieldDefaults.colors(focusedTextColor = StudioWhite, unfocusedTextColor = StudioWhite, focusedContainerColor = StudioSurfaceVariant, unfocusedContainerColor = StudioSurfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = subServices,
                        onValueChange = { subServices = it },
                        label = { Text("Included deliverables") },
                        colors = TextFieldDefaults.colors(focusedTextColor = StudioWhite, unfocusedTextColor = StudioWhite, focusedContainerColor = StudioSurfaceVariant, unfocusedContainerColor = StudioSurfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            viewModel.saveService(
                                ServiceEntity(
                                    category = category,
                                    title = title.trim(),
                                    description = description.trim(),
                                    basePrice = priceStr.toDoubleOrNull() ?: 20000.0,
                                    colorHex = "#FF9800",
                                    subServices = subServices.trim()
                                )
                            )
                            isAddOpen = false
                        },
                        enabled = title.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = ErmiOrange),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Save Service", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// 2. PAYMENTS SUB-SCREEN
@Composable
fun PaymentsSubScreen(viewModel: ErmiViewModel, modifier: Modifier) {
    val payments by viewModel.payments.collectAsState()
    val bookings by viewModel.bookings.collectAsState()

    val totalCollected = payments.sumOf { it.amount }
    val totalRemaining = bookings.sumOf { it.remainingBalance }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        SubScreenHeader(
            title = "Payment Tracking & Receipts",
            onBack = { viewModel.setMoreSubView(MoreSubView.NONE) },
            actionIcon = Icons.Default.Add,
            onAction = { viewModel.openAddPayment() }
        )

        // Summary Card
        Card(
            colors = CardDefaults.cardColors(containerColor = StudioSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().border(0.5.dp, StudioBorder, RoundedCornerShape(16.dp)).padding(vertical = 4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(14.dp)
            ) {
                Column {
                    Text(text = "Total Collected", fontSize = 11.sp, color = StudioTextMuted)
                    Text(text = "ETB %,.0f".format(totalCollected), fontSize = 18.sp, fontWeight = FontWeight.Black, color = StatusConfirmed)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Pending Receivables", fontSize = 11.sp, color = StudioTextMuted)
                    Text(text = "ETB %,.0f".format(totalRemaining), fontSize = 18.sp, fontWeight = FontWeight.Black, color = ErmiOrange)
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "PAYMENT RECEIPTS HISTORY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextMuted)

        LazyColumn(
            contentPadding = PaddingValues(bottom = 95.dp, top = 6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(payments, key = { it.id }) { payment ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().clickable { viewModel.inspectReceipt(payment) }.border(0.5.dp, StudioBorder, RoundedCornerShape(14.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = payment.receiptNumber, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioGold)
                            Text(text = payment.customerName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                            Text(text = "${payment.ethDate} • ${payment.paymentMethod}", fontSize = 11.sp, color = StudioTextSecondary)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "ETB %,.0f".format(payment.amount),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = StatusConfirmed
                            )
                            StatusBadge(status = payment.paymentStatus)
                        }
                    }
                }
            }
        }
    }
}

// 3. STAFF SUB-SCREEN
@Composable
fun StaffSubScreen(viewModel: ErmiViewModel, modifier: Modifier) {
    val staffList by viewModel.staff.collectAsState()
    val context = LocalContext.current
    var isAddOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        SubScreenHeader(
            title = "Staff & Production Crew",
            onBack = { viewModel.setMoreSubView(MoreSubView.NONE) },
            actionIcon = Icons.Default.Add,
            onAction = { isAddOpen = true }
        )

        LazyColumn(
            contentPadding = PaddingValues(bottom = 95.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(staffList, key = { it.id }) { member ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().border(0.5.dp, StudioBorder, RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(42.dp).clip(CircleShape).background(ErmiPurpleContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = member.name.take(1), fontWeight = FontWeight.Bold, color = StudioWhite, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = member.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                            Text(text = member.role, fontSize = 12.sp, color = ErmiOrangeLight, fontWeight = FontWeight.SemiBold)
                            Text(text = member.phone, fontSize = 11.sp, color = StudioTextMuted)
                        }
                        IconButton(onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${member.phone.replace(" ", "")}"))
                            context.startActivity(intent)
                        }) {
                            Icon(imageVector = Icons.Default.Call, contentDescription = "Call", tint = StatusConfirmed)
                        }
                    }
                }
            }
        }
    }

    if (isAddOpen) {
        var name by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("+251 ") }
        var role by remember { mutableStateOf("Camera Operator") }

        Dialog(onDismissRequest = { isAddOpen = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, ErmiPurpleLight.copy(alpha = 0.5f), RoundedCornerShape(20.dp)).padding(4.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = "Register Crew Member", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name *") },
                        colors = TextFieldDefaults.colors(focusedTextColor = StudioWhite, unfocusedTextColor = StudioWhite, focusedContainerColor = StudioSurfaceVariant, unfocusedContainerColor = StudioSurfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number *") },
                        colors = TextFieldDefaults.colors(focusedTextColor = StudioWhite, unfocusedTextColor = StudioWhite, focusedContainerColor = StudioSurfaceVariant, unfocusedContainerColor = StudioSurfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = role,
                        onValueChange = { role = it },
                        label = { Text("Role (Camera, Editor, DP, Audio...)") },
                        colors = TextFieldDefaults.colors(focusedTextColor = StudioWhite, unfocusedTextColor = StudioWhite, focusedContainerColor = StudioSurfaceVariant, unfocusedContainerColor = StudioSurfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            viewModel.saveStaff(StaffEntity(name = name.trim(), phone = phone.trim(), role = role.trim(), availability = "Available"))
                            isAddOpen = false
                        },
                        enabled = name.isNotBlank() && phone.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = ErmiOrange),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Save Crew Member", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// 4. EQUIPMENT SUB-SCREEN
@Composable
fun EquipmentSubScreen(viewModel: ErmiViewModel, modifier: Modifier) {
    val gearList by viewModel.equipment.collectAsState()
    var isAddOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        SubScreenHeader(
            title = "Equipment Inventory",
            onBack = { viewModel.setMoreSubView(MoreSubView.NONE) },
            actionIcon = Icons.Default.Add,
            onAction = { isAddOpen = true }
        )

        LazyColumn(
            contentPadding = PaddingValues(bottom = 95.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(gearList, key = { it.id }) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().border(0.5.dp, StudioBorder, RoundedCornerShape(14.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                            Text(text = "${item.category} • Condition: ${item.condition}", fontSize = 11.sp, color = StudioTextSecondary)
                            if (item.notes.isNotBlank()) {
                                Text(text = item.notes, fontSize = 10.sp, color = StudioTextMuted)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (item.status == "Available") StatusConfirmed.copy(alpha = 0.2f) else ErmiOrange.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = item.status,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (item.status == "Available") StatusConfirmed else ErmiOrange
                            )
                        }
                    }
                }
            }
        }
    }

    if (isAddOpen) {
        var name by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Camera") }
        var notes by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { isAddOpen = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, ErmiPurpleLight.copy(alpha = 0.5f), RoundedCornerShape(20.dp)).padding(4.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = "Add Equipment Item", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Equipment Name (e.g. Sony FX3)") },
                        colors = TextFieldDefaults.colors(focusedTextColor = StudioWhite, unfocusedTextColor = StudioWhite, focusedContainerColor = StudioSurfaceVariant, unfocusedContainerColor = StudioSurfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category (Camera, Lens, Gimbal, Lighting, Audio)") },
                        colors = TextFieldDefaults.colors(focusedTextColor = StudioWhite, unfocusedTextColor = StudioWhite, focusedContainerColor = StudioSurfaceVariant, unfocusedContainerColor = StudioSurfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Condition / Notes") },
                        colors = TextFieldDefaults.colors(focusedTextColor = StudioWhite, unfocusedTextColor = StudioWhite, focusedContainerColor = StudioSurfaceVariant, unfocusedContainerColor = StudioSurfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            viewModel.saveEquipment(EquipmentEntity(name = name.trim(), category = category.trim(), status = "Available", condition = "Excellent", notes = notes.trim()))
                            isAddOpen = false
                        },
                        enabled = name.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = ErmiOrange),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Save Equipment", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// 5. REPORTS SUB-SCREEN
@Composable
fun ReportsSubScreen(viewModel: ErmiViewModel, modifier: Modifier) {
    val bookings by viewModel.bookings.collectAsState()
    val payments by viewModel.payments.collectAsState()
    val context = LocalContext.current

    val totalBookings = bookings.size
    val totalRevenue = payments.sumOf { it.amount }
    val totalRemaining = bookings.sumOf { it.remainingBalance }
    val completedProjects = bookings.count { it.bookingStatus == "Completed" }

    // Service category breakdown
    val categoryCounts = bookings.groupBy { it.serviceCategory }.mapValues { it.value.size }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        SubScreenHeader(
            title = "Reports & Analytics",
            onBack = { viewModel.setMoreSubView(MoreSubView.NONE) },
            actionIcon = Icons.Default.Share,
            onAction = {
                val reportText = """
                ERMI VIDEO PRODUCTION - BUSINESS REPORT
                Company: Ermi Video Production
                Location: Eldana Mall 2nd Floor, Office 17, Hosaena
                Total Bookings: $totalBookings
                Completed Projects: $completedProjects
                Total Revenue Collected: ETB %,.2f
                Outstanding Receivables: ETB %,.2f
                Top Categories:
                ${categoryCounts.entries.joinToString("\n") { "• ${it.key}: ${it.value} projects" }}
                Contact: +251 917 800 851 / +251 910 040 877
                """.trimIndent().format(totalRevenue, totalRemaining)

                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, reportText)
                }
                context.startActivity(Intent.createChooser(sendIntent, "Export Business Report"))
            }
        )

        LazyColumn(
            contentPadding = PaddingValues(bottom = 95.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().border(0.5.dp, StudioBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "EXECUTIVE SUMMARY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ErmiOrange)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Total Projects Registered:", fontSize = 12.sp, color = StudioTextSecondary)
                            Text(text = "$totalBookings", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                        }
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Completed Deliveries:", fontSize = 12.sp, color = StudioTextSecondary)
                            Text(text = "$completedProjects", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = StatusConfirmed)
                        }
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Total Revenue (ETB):", fontSize = 12.sp, color = StudioTextSecondary)
                            Text(text = "ETB %,.0f".format(totalRevenue), fontSize = 14.sp, fontWeight = FontWeight.Black, color = StatusConfirmed)
                        }
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Outstanding Balance (ETB):", fontSize = 12.sp, color = StudioTextSecondary)
                            Text(text = "ETB %,.0f".format(totalRemaining), fontSize = 14.sp, fontWeight = FontWeight.Black, color = ErmiOrange)
                        }
                    }
                }
            }

            item {
                Text(text = "SERVICE POPULARITY BREAKDOWN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextMuted)
            }

            items(categoryCounts.entries.toList()) { entry ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().border(0.5.dp, StudioBorder, RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(CategoryColors.get(entry.key)))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = entry.key, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                        }
                        Text(text = "${entry.value} bookings", fontSize = 12.sp, color = ErmiOrangeLight, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// 6. SETTINGS SUB-SCREEN
@Composable
fun SettingsSubScreen(viewModel: ErmiViewModel, modifier: Modifier) {
    val activeRole by viewModel.currentRole.collectAsState()
    val showAmharic by viewModel.showAmharicCalendar.collectAsState()
    val isPrimaryEth by viewModel.primaryCalendarIsEth.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        SubScreenHeader(
            title = "Settings & Company Profile",
            onBack = { viewModel.setMoreSubView(MoreSubView.NONE) },
            actionIcon = null,
            onAction = {}
        )

        LazyColumn(
            contentPadding = PaddingValues(bottom = 95.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // User Role Setting
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().border(0.5.dp, StudioBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "CURRENT USER ROLE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ErmiOrange)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            UserRole.values().forEach { role ->
                                val isSelected = activeRole == role
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) ErmiOrange else StudioSurfaceVariant)
                                        .clickable { viewModel.setRole(role) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = role.label,
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

            // Calendar Preferences
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().border(0.5.dp, StudioBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "CALENDAR & REGION PREFERENCES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ErmiOrange)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Ethiopian Calendar as Primary", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                                Text(text = "Default date format across all bookings and reports", fontSize = 11.sp, color = StudioTextMuted)
                            }
                            Switch(
                                checked = isPrimaryEth,
                                onCheckedChange = { viewModel.togglePrimaryCalendar() },
                                colors = SwitchDefaults.colors(checkedThumbColor = ErmiOrange, checkedTrackColor = ErmiOrange.copy(alpha = 0.5f))
                            )
                        }

                        Divider(color = StudioBorder.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Amharic Month Labels (አማርኛ)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                                Text(text = "Show መስከረም, ጥቅምት, ኅዳር... in calendar headers", fontSize = 11.sp, color = StudioTextMuted)
                            }
                            Switch(
                                checked = showAmharic,
                                onCheckedChange = { viewModel.toggleAmharicCalendar() },
                                colors = SwitchDefaults.colors(checkedThumbColor = ErmiOrange, checkedTrackColor = ErmiOrange.copy(alpha = 0.5f))
                            )
                        }

                        Divider(color = StudioBorder.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 10.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Currency:", fontSize = 12.sp, color = StudioTextSecondary)
                            Text(text = "Ethiopian Birr (ETB)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StudioGold)
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Timezone:", fontSize = 12.sp, color = StudioTextSecondary)
                            Text(text = "Africa/Addis_Ababa", fontSize = 12.sp, color = StudioWhite)
                        }
                    }
                }
            }

            // Company Profile Info
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().border(0.5.dp, StudioBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "OFFICIAL COMPANY INFORMATION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ErmiOrange)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = "Ermi Video Production", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Address: Eldana Mall, 2nd Floor, Office No. 17\n(Located behind Nib International Bank, opposite Anda Mall, near Colonel Bezabih Petros Square)\nHosaena City, Ethiopia",
                            fontSize = 11.sp,
                            color = StudioTextSecondary,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+251917800851"))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = StatusConfirmed),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "0917 800 851", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+251910040877"))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = StudioSurfaceVariant),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = StudioWhite, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "0910 040 877", fontSize = 11.sp, color = StudioWhite, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubScreenHeader(
    title: String,
    onBack: () -> Unit,
    actionIcon: ImageVector?,
    onAction: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = StudioWhite)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
        }

        if (actionIcon != null) {
            IconButton(onClick = onAction, modifier = Modifier.size(36.dp)) {
                Icon(imageVector = actionIcon, contentDescription = null, tint = ErmiOrange)
            }
        }
    }
}
