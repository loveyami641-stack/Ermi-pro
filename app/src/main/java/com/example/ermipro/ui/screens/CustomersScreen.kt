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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import com.example.ermipro.data.model.CustomerEntity
import com.example.ermipro.ui.components.BookingCardCompact
import com.example.ermipro.ui.viewmodel.ErmiViewModel
import com.example.ui.theme.ErmiOrange
import com.example.ui.theme.ErmiOrangeLight
import com.example.ui.theme.ErmiPurple
import com.example.ui.theme.ErmiPurpleContainer
import com.example.ui.theme.ErmiPurpleLight
import com.example.ui.theme.StatusCancelled
import com.example.ui.theme.StatusConfirmed
import com.example.ui.theme.StudioBackground
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioGold
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextSecondary
import com.example.ui.theme.StudioWhite

@Composable
fun CustomersScreen(
    viewModel: ErmiViewModel,
    modifier: Modifier = Modifier
) {
    val customers by viewModel.customers.collectAsState()
    val bookings by viewModel.bookings.collectAsState()
    val searchQuery by viewModel.customerSearchQuery.collectAsState()
    val context = LocalContext.current

    var inspectingCustomer by remember { mutableStateOf<CustomerEntity?>(null) }

    val filteredCustomers = customers.filter { c ->
        searchQuery.isBlank() ||
                c.name.contains(searchQuery, ignoreCase = true) ||
                c.phone.contains(searchQuery, ignoreCase = true) ||
                c.email.contains(searchQuery, ignoreCase = true) ||
                c.address.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = StudioBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddCustomer() },
                containerColor = ErmiOrange,
                contentColor = Color.Black,
                modifier = Modifier.padding(bottom = 80.dp).testTag("fab_new_customer")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Customer")
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
                    onValueChange = { viewModel.setCustomerSearchQuery(it) },
                    placeholder = { Text("Search client name, phone number, location...", fontSize = 12.sp) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = StudioTextMuted) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setCustomerSearchQuery("") }) {
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
                    modifier = Modifier.fillMaxWidth().testTag("customer_search_input")
                )
            }

            if (filteredCustomers.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = StudioTextMuted, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "No customers found", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Register a new client using the + button.", fontSize = 12.sp, color = StudioTextSecondary)
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 95.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredCustomers, key = { it.id }) { customer ->
                        // Calculate live stats from bookings table for accuracy
                        val customerBookings = bookings.filter { it.customerName.equals(customer.name, ignoreCase = true) || it.customerPhone == customer.phone }
                        val liveSpent = customerBookings.sumOf { it.paidAmount }
                        val liveBalance = customerBookings.sumOf { it.remainingBalance }

                        CustomerCard(
                            customer = customer,
                            bookingsCount = customerBookings.size,
                            totalSpent = liveSpent,
                            outstandingBalance = liveBalance,
                            onClick = { inspectingCustomer = customer },
                            onCall = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${customer.phone.replace(" ", "")}"))
                                context.startActivity(intent)
                            },
                            onMessage = {
                                val cleanPhone = customer.phone.replace("+", "").replace(" ", "")
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$cleanPhone"))
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }

    // Customer Inspection & Project History Modal
    inspectingCustomer?.let { customer ->
        val customerBookings = bookings.filter { it.customerName.equals(customer.name, ignoreCase = true) || it.customerPhone == customer.phone }
        Dialog(onDismissRequest = { inspectingCustomer = null }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ErmiPurpleLight.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
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
                            Text(text = customer.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                            Text(text = customer.phone, fontSize = 13.sp, color = ErmiOrangeLight)
                        }
                        IconButton(onClick = {
                            viewModel.deleteCustomer(customer)
                            inspectingCustomer = null
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = StatusCancelled)
                        }
                    }

                    if (customer.email.isNotBlank()) {
                        Text(text = "Email: ${customer.email}", fontSize = 12.sp, color = StudioTextSecondary)
                    }
                    if (customer.address.isNotBlank()) {
                        Text(text = "Address: ${customer.address}", fontSize = 12.sp, color = StudioTextMuted)
                    }
                    if (customer.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Notes: ${customer.notes}", fontSize = 11.sp, color = StudioGold)
                    }

                    Divider(color = StudioBorder, modifier = Modifier.padding(vertical = 12.dp))

                    Text(
                        text = "CUSTOMER PROJECT HISTORY (${customerBookings.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextMuted
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (customerBookings.isEmpty()) {
                        Text(text = "No booking records yet.", fontSize = 12.sp, color = StudioTextSecondary)
                    } else {
                        customerBookings.forEach { b ->
                            BookingCardCompact(
                                booking = b,
                                onClick = {
                                    inspectingCustomer = null
                                    viewModel.inspectBooking(b)
                                }
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
fun CustomerCard(
    customer: CustomerEntity,
    bookingsCount: Int,
    totalSpent: Double,
    outstandingBalance: Double,
    onClick: () -> Unit,
    onCall: () -> Unit,
    onMessage: () -> Unit
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ErmiPurpleContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = customer.name.take(1).uppercase(),
                            fontWeight = FontWeight.Bold,
                            color = StudioWhite,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = customer.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                        Text(text = customer.phone, fontSize = 12.sp, color = StudioTextSecondary)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onCall, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = "Call", tint = StatusConfirmed, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = onMessage, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Message, contentDescription = "WhatsApp", tint = ErmiPurpleLight, modifier = Modifier.size(16.dp))
                    }
                }
            }

            if (customer.address.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = customer.address, fontSize = 11.sp, color = StudioTextMuted)
            }

            Divider(color = StudioBorder.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$bookingsCount Project(s)",
                    fontSize = 11.sp,
                    color = StudioTextSecondary
                )
                Text(
                    text = "Spent: ETB %,.0f".format(totalSpent),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = StatusConfirmed
                )
                Text(
                    text = "Balance: ETB %,.0f".format(outstandingBalance),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (outstandingBalance > 0) ErmiOrange else StatusConfirmed
                )
            }
        }
    }
}
