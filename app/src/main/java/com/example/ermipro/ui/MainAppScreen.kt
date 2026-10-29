package com.example.ermipro.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ermipro.calendar.EthiopianCalendarHelper
import com.example.ermipro.ui.components.AddCustomerDialog
import com.example.ermipro.ui.components.AddEditBookingDialog
import com.example.ermipro.ui.components.AddPaymentDialog
import com.example.ermipro.ui.components.BookingDetailDialog
import com.example.ermipro.ui.components.ErmiTopBar
import com.example.ermipro.ui.components.OfficialReceiptDialog
import com.example.ermipro.ui.screens.BookingsScreen
import com.example.ermipro.ui.screens.CalendarScreen
import com.example.ermipro.ui.screens.CustomersScreen
import com.example.ermipro.ui.screens.DashboardScreen
import com.example.ermipro.ui.screens.MoreScreen
import com.example.ermipro.ui.viewmodel.ErmiViewModel
import com.example.ermipro.ui.viewmodel.MainTab
import com.example.ui.theme.ErmiOrange
import com.example.ui.theme.ErmiPurpleLight
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioWhite

@Composable
fun MainAppScreen(
    viewModel: ErmiViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val activeRole by viewModel.currentRole.collectAsState()
    val showAmharic by viewModel.showAmharicCalendar.collectAsState()
    val todayEth = EthiopianCalendarHelper.getTodayEth()

    // Dialog state collectors
    val inspectedBooking by viewModel.inspectedBooking.collectAsState()
    val inspectedReceipt by viewModel.inspectedReceipt.collectAsState()
    val isAddBookingOpen by viewModel.isAddBookingOpen.collectAsState()
    val bookingToEdit by viewModel.bookingToEdit.collectAsState()
    val isAddCustomerOpen by viewModel.isAddCustomerOpen.collectAsState()
    val isAddPaymentOpen by viewModel.isAddPaymentOpen.collectAsState()

    val services by viewModel.services.collectAsState()
    val staffList by viewModel.staff.collectAsState()
    val bookings by viewModel.bookings.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        containerColor = StudioBackground,
        topBar = {
            ErmiTopBar(
                activeRole = activeRole,
                onRoleSelected = { viewModel.setRole(it) },
                todayEthDateString = todayEth.toDisplayString(showAmharic)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = StudioSurface,
                contentColor = StudioWhite,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_nav")
            ) {
                val navItems = listOf(
                    Triple(MainTab.DASHBOARD, Icons.Default.Dashboard, Icons.Outlined.Dashboard),
                    Triple(MainTab.CALENDAR, Icons.Default.CalendarMonth, Icons.Outlined.CalendarMonth),
                    Triple(MainTab.BOOKINGS, Icons.Default.Videocam, Icons.Outlined.Videocam),
                    Triple(MainTab.CUSTOMERS, Icons.Default.People, Icons.Outlined.People),
                    Triple(MainTab.MORE, Icons.Default.Menu, Icons.Outlined.Menu)
                )

                navItems.forEach { (tab, filledIcon, outlinedIcon) ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setTab(tab) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) filledIcon else outlinedIcon,
                                contentDescription = tab.label
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = ErmiOrange,
                            indicatorColor = ErmiOrange,
                            unselectedIconColor = StudioTextMuted,
                            unselectedTextColor = StudioTextMuted
                        ),
                        modifier = Modifier.testTag("nav_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainTab.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                MainTab.CALENDAR -> CalendarScreen(viewModel = viewModel)
                MainTab.BOOKINGS -> BookingsScreen(viewModel = viewModel)
                MainTab.CUSTOMERS -> CustomersScreen(viewModel = viewModel)
                MainTab.MORE -> MoreScreen(viewModel = viewModel)
            }
        }
    }

    // Modal Dialogs
    inspectedBooking?.let { b ->
        BookingDetailDialog(
            booking = b,
            onDismiss = { viewModel.inspectBooking(null) },
            onEdit = { viewModel.openEditBooking(it) },
            onStatusChange = { booking, newStatus ->
                viewModel.updateBookingStatus(booking, newStatus)
            },
            onDelete = { viewModel.deleteBooking(it) }
        )
    }

    inspectedReceipt?.let { p ->
        OfficialReceiptDialog(
            receipt = p,
            onDismiss = { viewModel.inspectReceipt(null) }
        )
    }

    if (isAddBookingOpen) {
        AddEditBookingDialog(
            initialBooking = bookingToEdit,
            presetEthDate = null,
            services = services,
            staffList = staffList,
            onDismiss = { viewModel.closeAddBooking() },
            onSave = { viewModel.saveBooking(it) },
            checkConflict = { year, month, day, crew, gear, excludeId ->
                viewModel.checkCrewOrEquipmentConflict(year, month, day, crew, gear, excludeId)
            }
        )
    }

    if (isAddCustomerOpen) {
        AddCustomerDialog(
            initialCustomer = null,
            onDismiss = { viewModel.closeAddCustomer() },
            onSave = { viewModel.saveCustomer(it) }
        )
    }

    if (isAddPaymentOpen) {
        AddPaymentDialog(
            bookings = bookings,
            onDismiss = { viewModel.closeAddPayment() },
            onSave = { viewModel.recordPayment(it) }
        )
    }
}
