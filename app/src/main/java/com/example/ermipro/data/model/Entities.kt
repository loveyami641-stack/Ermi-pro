package com.example.ermipro.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookingNumber: String,
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String = "",
    val serviceCategory: String, // e.g. "Music Video", "Wedding", "TV Commercial", "Documentary", "Birthday", "Graduation", "Portrait", "Studio Photography", "Other"
    val serviceTitle: String,
    val ethYear: Int,
    val ethMonth: Int,
    val ethDay: Int,
    val gregYear: Int,
    val gregMonth: Int,
    val gregDay: Int,
    val startTime: String, // e.g. "09:00 AM"
    val endTime: String,   // e.g. "03:00 PM"
    val location: String,  // e.g. "Hosaena - Eldana Mall Studio", "Eldana Mall 2nd Floor", "Bezabih Petros Square", "Addis Ababa"
    val requiredService: String,
    val additionalServices: String = "",
    val assignedCameraOperator: String = "",
    val assignedPhotographer: String = "",
    val assignedEditor: String = "",
    val assignedEquipment: String = "",
    val projectDescription: String = "",
    val estimatedPrice: Double = 0.0,
    val depositAmount: Double = 0.0,
    val paidAmount: Double = 0.0,
    val bookingStatus: String = "Confirmed", // "New Request", "Pending", "Confirmed", "In Progress", "Completed", "Cancelled"
    val paymentStatus: String = "Unpaid", // "Unpaid", "Partially Paid", "Fully Paid"
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    val remainingBalance: Double
        get() = (estimatedPrice - paidAmount).coerceAtLeast(0.0)

    val ethDateString: String
        get() = "${com.example.ermipro.calendar.EthiopianCalendarHelper.ETH_MONTH_NAMES.getOrElse(ethMonth - 1) { "Month $ethMonth" }} $ethDay, $ethYear"

    val gregDateString: String
        get() = "$gregYear-%02d-%02d".format(gregMonth, gregDay)
}

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val email: String = "",
    val address: String = "",
    val notes: String = "",
    val totalBookings: Int = 0,
    val totalSpent: Double = 0.0,
    val outstandingBalance: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val title: String,
    val description: String,
    val basePrice: Double,
    val colorHex: String,
    val subServices: String = ""
)

@Entity(tableName = "staff")
data class StaffEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val role: String, // "Camera Operator", "Photographer", "Video Editor", "Director", "Production Assistant", "Colorist", "Drone Pilot"
    val availability: String = "Available", // "Available", "On Shoot", "Off Duty"
    val notes: String = ""
)

@Entity(tableName = "equipment")
data class EquipmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String, // "Camera", "Lens", "Gimbal", "Lighting", "Audio", "Drone", "Tripod"
    val condition: String = "Excellent", // "Excellent", "Good", "Needs Service"
    val status: String = "Available", // "Available", "In Use", "Maintenance"
    val assignedBookingId: Long? = null,
    val notes: String = ""
)

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookingId: Long,
    val receiptNumber: String,
    val customerName: String,
    val amount: Double,
    val ethDate: String,
    val gregDate: String,
    val paymentMethod: String, // "Cash", "CBE Bank Transfer", "Telebirr", "Awash Bank", "Nib Bank", "Mobile Banking"
    val referenceNumber: String = "",
    val paymentStatus: String = "Deposit", // "Deposit", "Partial", "Fully Paid"
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
