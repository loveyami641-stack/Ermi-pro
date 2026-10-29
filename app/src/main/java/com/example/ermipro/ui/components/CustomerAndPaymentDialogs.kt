package com.example.ermipro.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ermipro.calendar.EthiopianCalendarHelper
import com.example.ermipro.data.model.BookingEntity
import com.example.ermipro.data.model.CustomerEntity
import com.example.ermipro.data.model.PaymentEntity
import com.example.ui.theme.ErmiOrange
import com.example.ui.theme.ErmiOrangeLight
import com.example.ui.theme.ErmiPurple
import com.example.ui.theme.ErmiPurpleLight
import com.example.ui.theme.StatusConfirmed
import com.example.ui.theme.StudioCardElevated
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextSecondary
import com.example.ui.theme.StudioWhite

@Composable
fun AddCustomerDialog(
    initialCustomer: CustomerEntity? = null,
    onDismiss: () -> Unit,
    onSave: (CustomerEntity) -> Unit
) {
    var name by remember { mutableStateOf(initialCustomer?.name ?: "") }
    var phone by remember { mutableStateOf(initialCustomer?.phone ?: "") }
    var email by remember { mutableStateOf(initialCustomer?.email ?: "") }
    var address by remember { mutableStateOf(initialCustomer?.address ?: "Hosaena, Ethiopia") }
    var notes by remember { mutableStateOf(initialCustomer?.notes ?: "") }

    Dialog(onDismissRequest = onDismiss) {
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
                    .padding(20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (initialCustomer == null) "Register New Customer" else "Edit Customer",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioWhite
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = StudioTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Customer Name *") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = StudioWhite,
                        unfocusedTextColor = StudioWhite,
                        focusedContainerColor = StudioSurfaceVariant,
                        unfocusedContainerColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("customer_name_input")
                )

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number (+251...) *") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = StudioWhite,
                        unfocusedTextColor = StudioWhite,
                        focusedContainerColor = StudioSurfaceVariant,
                        unfocusedContainerColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("customer_phone_input")
                )

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = StudioWhite,
                        unfocusedTextColor = StudioWhite,
                        focusedContainerColor = StudioSurfaceVariant,
                        unfocusedContainerColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address / Location") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = StudioWhite,
                        unfocusedTextColor = StudioWhite,
                        focusedContainerColor = StudioSurfaceVariant,
                        unfocusedContainerColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Customer Notes (Preferences, Events)") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = StudioWhite,
                        unfocusedTextColor = StudioWhite,
                        focusedContainerColor = StudioSurfaceVariant,
                        unfocusedContainerColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = {
                        val customer = CustomerEntity(
                            id = initialCustomer?.id ?: 0L,
                            name = name.trim(),
                            phone = phone.trim(),
                            email = email.trim(),
                            address = address.trim(),
                            notes = notes.trim(),
                            totalBookings = initialCustomer?.totalBookings ?: 0,
                            totalSpent = initialCustomer?.totalSpent ?: 0.0,
                            outstandingBalance = initialCustomer?.outstandingBalance ?: 0.0
                        )
                        onSave(customer)
                    },
                    enabled = name.isNotBlank() && phone.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = ErmiOrange),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("save_customer_submit_button")
                ) {
                    Text(text = "Save Customer Profile", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentDialog(
    bookings: List<BookingEntity>,
    onDismiss: () -> Unit,
    onSave: (PaymentEntity) -> Unit
) {
    var selectedBooking by remember { mutableStateOf(bookings.firstOrNull()) }
    var bookingDropdown by remember { mutableStateOf(false) }

    var amountStr by remember { mutableStateOf(selectedBooking?.remainingBalance?.toInt()?.toString() ?: "10000") }
    var paymentMethod by remember { mutableStateOf("CBE Bank Transfer") }
    var methodDropdown by remember { mutableStateOf(false) }
    var referenceNumber by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val methods = listOf("CBE Bank Transfer", "Telebirr", "Cash", "Awash Bank", "Nib International Bank", "Mobile Banking")
    val todayEth = EthiopianCalendarHelper.getTodayEth()
    val todayGreg = EthiopianCalendarHelper.getTodayGreg()

    Dialog(onDismissRequest = onDismiss) {
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
                    .padding(20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Record Customer Payment",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioWhite
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = StudioTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                // Booking Selector
                Text(text = "ASSOCIATED BOOKING", fontSize = 11.sp, color = StudioTextMuted, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = bookingDropdown,
                    onExpandedChange = { bookingDropdown = !bookingDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedBooking?.let { "${it.customerName} • ${it.serviceTitle}" } ?: "Select Booking",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bookingDropdown) },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = StudioWhite,
                            unfocusedTextColor = StudioWhite,
                            focusedContainerColor = StudioSurfaceVariant,
                            unfocusedContainerColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = bookingDropdown,
                        onDismissRequest = { bookingDropdown = false },
                        modifier = Modifier.background(StudioCardElevated)
                    ) {
                        bookings.forEach { b ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(text = "${b.customerName} (${b.bookingNumber})", fontWeight = FontWeight.Bold, color = StudioWhite)
                                        Text(text = "Balance: ETB %,.0f • ${b.serviceTitle}".format(b.remainingBalance), fontSize = 11.sp, color = ErmiOrangeLight)
                                    }
                                },
                                onClick = {
                                    selectedBooking = b
                                    amountStr = b.remainingBalance.toInt().toString()
                                    bookingDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("Amount Received (ETB) *") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = StudioWhite,
                        unfocusedTextColor = StudioWhite,
                        focusedContainerColor = StudioSurfaceVariant,
                        unfocusedContainerColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("payment_amount_input")
                )

                Spacer(modifier = Modifier.height(10.dp))
                // Payment Method
                ExposedDropdownMenuBox(
                    expanded = methodDropdown,
                    onExpandedChange = { methodDropdown = !methodDropdown }
                ) {
                    OutlinedTextField(
                        value = paymentMethod,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Payment Method") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = methodDropdown) },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = StudioWhite,
                            unfocusedTextColor = StudioWhite,
                            focusedContainerColor = StudioSurfaceVariant,
                            unfocusedContainerColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = methodDropdown,
                        onDismissRequest = { methodDropdown = false },
                        modifier = Modifier.background(StudioCardElevated)
                    ) {
                        methods.forEach { m ->
                            DropdownMenuItem(
                                text = { Text(text = m, color = StudioWhite) },
                                onClick = {
                                    paymentMethod = m
                                    methodDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = referenceNumber,
                    onValueChange = { referenceNumber = it },
                    label = { Text("Transaction / Bank Reference No.") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = StudioWhite,
                        unfocusedTextColor = StudioWhite,
                        focusedContainerColor = StudioSurfaceVariant,
                        unfocusedContainerColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Payment Notes") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = StudioWhite,
                        unfocusedTextColor = StudioWhite,
                        focusedContainerColor = StudioSurfaceVariant,
                        unfocusedContainerColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))
                val parsedAmount = amountStr.toDoubleOrNull() ?: 0.0
                Button(
                    onClick = {
                        val receiptNo = "REC-EVP-%05d".format((1000..99999).random())
                        val p = PaymentEntity(
                            bookingId = selectedBooking?.id ?: 0L,
                            receiptNumber = receiptNo,
                            customerName = selectedBooking?.customerName ?: "Valued Customer",
                            amount = parsedAmount,
                            ethDate = todayEth.toDisplayString(),
                            gregDate = "${todayGreg.year}-${todayGreg.month}-${todayGreg.day}",
                            paymentMethod = paymentMethod,
                            referenceNumber = referenceNumber.trim(),
                            paymentStatus = if (parsedAmount >= (selectedBooking?.remainingBalance ?: 0.0)) "Fully Paid" else "Partial",
                            notes = notes.trim()
                        )
                        onSave(p)
                    },
                    enabled = parsedAmount > 0,
                    colors = ButtonDefaults.buttonColors(containerColor = ErmiOrange),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("save_payment_submit_button")
                ) {
                    Text(text = "Generate Official Receipt", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}
