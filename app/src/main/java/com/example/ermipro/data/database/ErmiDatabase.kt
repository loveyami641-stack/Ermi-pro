package com.example.ermipro.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ermipro.calendar.EthiopianCalendarHelper
import com.example.ermipro.data.dao.BookingDao
import com.example.ermipro.data.dao.CustomerDao
import com.example.ermipro.data.dao.EquipmentDao
import com.example.ermipro.data.dao.PaymentDao
import com.example.ermipro.data.dao.ServiceDao
import com.example.ermipro.data.dao.StaffDao
import com.example.ermipro.data.model.BookingEntity
import com.example.ermipro.data.model.CustomerEntity
import com.example.ermipro.data.model.EquipmentEntity
import com.example.ermipro.data.model.PaymentEntity
import com.example.ermipro.data.model.ServiceEntity
import com.example.ermipro.data.model.StaffEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        BookingEntity::class,
        CustomerEntity::class,
        ServiceEntity::class,
        StaffEntity::class,
        EquipmentEntity::class,
        PaymentEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ErmiDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao
    abstract fun customerDao(): CustomerDao
    abstract fun serviceDao(): ServiceDao
    abstract fun staffDao(): StaffDao
    abstract fun equipmentDao(): EquipmentDao
    abstract fun paymentDao(): PaymentDao

    companion object {
        @Volatile
        private var INSTANCE: ErmiDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ErmiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ErmiDatabase::class.java,
                    "ermi_production_db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        private suspend fun populateInitialData(db: ErmiDatabase) {
            val serviceDao = db.serviceDao()
            val staffDao = db.staffDao()
            val equipmentDao = db.equipmentDao()
            val customerDao = db.customerDao()
            val bookingDao = db.bookingDao()
            val paymentDao = db.paymentDao()

            // 1. Initial Services
            val services = listOf(
                ServiceEntity(
                    category = "Music Video",
                    title = "Music Video Production",
                    description = "Gospel music videos, Mezmur clips, 4K multi-cam recording, professional color grading and full cinematic post-production.",
                    basePrice = 45000.0,
                    colorHex = "#9C27B0",
                    subServices = "Gospel Clips, Mezmur, 4K Multi-Cam, Color Grading, Storyboard"
                ),
                ServiceEntity(
                    category = "TV Commercial",
                    title = "Television Commercials",
                    description = "High-impact broadcast TV advertisements, product commercials, promotional and social media ad video production.",
                    basePrice = 35000.0,
                    colorHex = "#FF9800",
                    subServices = "TV Ads, Product Showcase, Business Promo, Drone Shots"
                ),
                ServiceEntity(
                    category = "Documentary",
                    title = "Documentary Films",
                    description = "In-depth documentary production, high-profile interviews, corporate anniversary documentaries, cultural & event archives.",
                    basePrice = 50000.0,
                    colorHex = "#00BCD4",
                    subServices = "Interviews, Archival Footage, Corporate Profile, Narration"
                ),
                ServiceEntity(
                    category = "Wedding",
                    title = "Wedding Programs",
                    description = "Complete wedding video recording, drone coverage, bridal highlights, cinematic wedding film editing and photo albums.",
                    basePrice = 40000.0,
                    colorHex = "#E91E63",
                    subServices = "Full Day 4K Recording, Highlights Reel, Drone Flyovers, Photo Album"
                ),
                ServiceEntity(
                    category = "Birthday",
                    title = "Birthday Programs",
                    description = "Birthday ceremony recording, candid photography, social media highlight reels and family moments.",
                    basePrice = 15000.0,
                    colorHex = "#4CAF50",
                    subServices = "Event Video, Portrait Session, Quick Teaser Reel"
                ),
                ServiceEntity(
                    category = "Graduation",
                    title = "Graduation Programs",
                    description = "Graduation ceremony video coverage, campus portrait photography, family photos and highlight videos.",
                    basePrice = 18000.0,
                    colorHex = "#3F51B5",
                    subServices = "Stage Coverage, Outdoor Portraits, Highlights Reel"
                ),
                ServiceEntity(
                    category = "Portrait",
                    title = "Portrait Photography",
                    description = "Individual, creative, outdoor, and executive portraits shot on prime cinema lenses with professional lighting.",
                    basePrice = 6000.0,
                    colorHex = "#FF5722",
                    subServices = "Outdoor Session, Executive Headshots, Retouched Photos"
                ),
                ServiceEntity(
                    category = "Studio Photography",
                    title = "Studio Photography",
                    description = "In-studio photography at Eldana Mall, family portraits, fashion lookbooks, commercial product photography.",
                    basePrice = 8500.0,
                    colorHex = "#795548",
                    subServices = "Studio Lights, Background Changes, Family Packages, Retouching"
                )
            )
            services.forEach { serviceDao.insertService(it) }

            // 2. Initial Staff
            val staffList = listOf(
                StaffEntity(name = "Ermias Tadesse", phone = "+251 917 800 851", role = "Director & Lead DP", availability = "Available", notes = "Founder & Chief Cinematographer"),
                StaffEntity(name = "Natnael Kassahun", phone = "+251 910 040 877", role = "Lead Camera Operator", availability = "Available", notes = "Gimbal & Ronin Specialist"),
                StaffEntity(name = "Biruk Mengistu", phone = "+251 911 234 567", role = "Senior Video Editor", availability = "Available", notes = "DaVinci Resolve & Premiere Pro Colorist"),
                StaffEntity(name = "Selamawit Abebe", phone = "+251 922 345 678", role = "Photographer", availability = "Available", notes = "Lead Studio & Wedding Photographer"),
                StaffEntity(name = "Yohannes Desta", phone = "+251 933 456 789", role = "Drone Pilot & Assistant", availability = "Available", notes = "Certified FPV & Aerial Cinematographer"),
                StaffEntity(name = "Abel Getachew", phone = "+251 944 567 890", role = "Audio Engineer", availability = "Available", notes = "Location Sound & Boom Operator")
            )
            staffList.forEach { staffDao.insertStaff(it) }

            // 3. Initial Equipment
            val equipmentList = listOf(
                EquipmentEntity(name = "Sony FX3 Cinema Line", category = "Camera", condition = "Excellent", status = "Available", notes = "Main A-Cam 4K 120p"),
                EquipmentEntity(name = "Sony Alpha A7S III", category = "Camera", condition = "Excellent", status = "Available", notes = "Low light B-Cam"),
                EquipmentEntity(name = "Canon EOS C70 Cinema", category = "Camera", condition = "Excellent", status = "Available", notes = "Documentary & Commercials"),
                EquipmentEntity(name = "Sony FE 24-70mm f/2.8 GM II", category = "Lens", condition = "Excellent", status = "Available", notes = "Workhorse zoom"),
                EquipmentEntity(name = "Sony FE 70-200mm f/2.8 GM II", category = "Lens", condition = "Excellent", status = "Available", notes = "Telephoto wedding/events"),
                EquipmentEntity(name = "Sony FE 50mm f/1.2 GM", category = "Lens", condition = "Excellent", status = "Available", notes = "Cinematic portrait prime"),
                EquipmentEntity(name = "DJI RS3 Pro Gimbal", category = "Gimbal", condition = "Excellent", status = "Available", notes = "With LiDAR focus motor"),
                EquipmentEntity(name = "Aputure LS 300d II Light", category = "Lighting", condition = "Excellent", status = "Available", notes = "Key light with Light Dome II"),
                EquipmentEntity(name = "Amaran 200x Bi-Color LED", category = "Lighting", condition = "Excellent", status = "Available", notes = "Fill/accent light"),
                EquipmentEntity(name = "Rode Wireless PRO Dual", category = "Audio", condition = "Excellent", status = "Available", notes = "32-bit float audio transmitter"),
                EquipmentEntity(name = "Sennheiser MKE 600 Mic", category = "Audio", condition = "Excellent", status = "Available", notes = "Boom mic with windscreen"),
                EquipmentEntity(name = "DJI Mavic 3 Pro Cine Drone", category = "Drone", condition = "Excellent", status = "Available", notes = "Apple ProRes aerial system")
            )
            equipmentList.forEach { equipmentDao.insertEquipment(it) }

            // 4. Initial Customers
            val customers = listOf(
                CustomerEntity(name = "Pastor Daniel Mekonnen", phone = "+251 912 345 678", email = "pastordaniel@gospel.et", address = "Hosaena, Wachemo Area", notes = "Regular client for Gospel Music Video releases", totalBookings = 2, totalSpent = 85000.0, outstandingBalance = 0.0),
                CustomerEntity(name = "Dr. Bethlehem Haile", phone = "+251 913 456 789", email = "bethlehem.h@wachemo.edu.et", address = "Hosaena, Near Colonel Bezabih Petros Sq", notes = "Wedding coverage booking and portraits", totalBookings = 1, totalSpent = 40000.0, outstandingBalance = 15000.0),
                CustomerEntity(name = "Eldana Commercial Group", phone = "+251 914 567 890", email = "marketing@eldanamall.et", address = "Eldana Mall, 1st Floor, Hosaena", notes = "TV advertisement for retail campaign", totalBookings = 1, totalSpent = 35000.0, outstandingBalance = 0.0),
                CustomerEntity(name = "Wachemo University Cultural Troupe", phone = "+251 915 678 901", email = "culture@wachemo.edu.et", address = "Hosaena University Campus", notes = "Documentary film series on cultural heritage", totalBookings = 1, totalSpent = 50000.0, outstandingBalance = 20000.0)
            )
            val c1Id = customerDao.insertCustomer(customers[0])
            val c2Id = customerDao.insertCustomer(customers[1])
            val c3Id = customerDao.insertCustomer(customers[2])
            val c4Id = customerDao.insertCustomer(customers[3])

            // Calculate Ethiopian dates around today (Tikimt 2019 / Oct 2026)
            val todayEth = EthiopianCalendarHelper.getTodayEth()
            val todayGreg = EthiopianCalendarHelper.getTodayGreg()

            // 5. Initial Bookings
            val b1 = BookingEntity(
                bookingNumber = "EVP-2019-001",
                customerName = "Pastor Daniel Mekonnen",
                customerPhone = "+251 912 345 678",
                customerEmail = "pastordaniel@gospel.et",
                serviceCategory = "Music Video",
                serviceTitle = "Gospel Music Video Recording",
                ethYear = todayEth.year,
                ethMonth = todayEth.month,
                ethDay = todayEth.day,
                gregYear = todayGreg.year,
                gregMonth = todayGreg.month,
                gregDay = todayGreg.day,
                startTime = "08:30 AM",
                endTime = "02:00 PM",
                location = "Eldana Mall 2nd Floor Studio & Outdoor Hosaena",
                requiredService = "Gospel Music Video (4K Multi-Cam)",
                additionalServices = "Color grading, Drone shots, Teaser promo",
                assignedCameraOperator = "Ermias Tadesse",
                assignedPhotographer = "Selamawit Abebe",
                assignedEditor = "Biruk Mengistu",
                assignedEquipment = "Sony FX3 Cinema Line, Sony FE 24-70mm f/2.8, DJI RS3 Pro, Rode Wireless PRO",
                projectDescription = "Main studio performance and scenic outdoor takes for upcoming album track.",
                estimatedPrice = 45000.0,
                depositAmount = 25000.0,
                paidAmount = 45000.0,
                bookingStatus = "In Progress",
                paymentStatus = "Fully Paid",
                notes = "Lighting setup starts at 07:45 AM. Vocal track provided on USB."
            )
            val b1Id = bookingDao.insertBooking(b1)

            val nextEthDay = if (todayEth.day < 28) todayEth.day + 2 else 5
            val nextGreg = EthiopianCalendarHelper.ethToGregorian(todayEth.year, todayEth.month, nextEthDay)

            val b2 = BookingEntity(
                bookingNumber = "EVP-2019-002",
                customerName = "Dr. Bethlehem Haile",
                customerPhone = "+251 913 456 789",
                customerEmail = "bethlehem.h@wachemo.edu.et",
                serviceCategory = "Wedding",
                serviceTitle = "Full Wedding Video & Photography",
                ethYear = todayEth.year,
                ethMonth = todayEth.month,
                ethDay = nextEthDay,
                gregYear = nextGreg.year,
                gregMonth = nextGreg.month,
                gregDay = nextGreg.day,
                startTime = "09:00 AM",
                endTime = "06:00 PM",
                location = "Hosaena Grand Hall & Colonel Bezabih Petros Sq",
                requiredService = "Wedding Film & Photography Package",
                additionalServices = "Drone aerials, Cinematic highlight trailer, Velvet photo album",
                assignedCameraOperator = "Natnael Kassahun",
                assignedPhotographer = "Selamawit Abebe",
                assignedEditor = "Biruk Mengistu",
                assignedEquipment = "Sony Alpha A7S III, Canon C70, DJI Mavic 3 Pro Cine, Aputure LS 300d II",
                projectDescription = "Bride preparation, church ceremony, park photos, and reception dinner.",
                estimatedPrice = 40000.0,
                depositAmount = 25000.0,
                paidAmount = 25000.0,
                bookingStatus = "Confirmed",
                paymentStatus = "Partially Paid",
                notes = "Deposit received via CBE Birr. Remaining balance due on delivery."
            )
            val b2Id = bookingDao.insertBooking(b2)

            val nextEthDay2 = if (todayEth.day < 25) todayEth.day + 5 else 8
            val nextGreg2 = EthiopianCalendarHelper.ethToGregorian(todayEth.year, todayEth.month, nextEthDay2)

            val b3 = BookingEntity(
                bookingNumber = "EVP-2019-003",
                customerName = "Eldana Commercial Group",
                customerPhone = "+251 914 567 890",
                customerEmail = "marketing@eldanamall.et",
                serviceCategory = "TV Commercial",
                serviceTitle = "Retail Season TV Advertisement",
                ethYear = todayEth.year,
                ethMonth = todayEth.month,
                ethDay = nextEthDay2,
                gregYear = nextGreg2.year,
                gregMonth = nextGreg2.month,
                gregDay = nextGreg2.day,
                startTime = "01:00 PM",
                endTime = "05:30 PM",
                location = "Eldana Mall, Hosaena",
                requiredService = "Commercial Video Production",
                additionalServices = "Voiceover, Motion graphics, 30s cutdown",
                assignedCameraOperator = "Ermias Tadesse",
                assignedPhotographer = "Natnael Kassahun",
                assignedEditor = "Biruk Mengistu",
                assignedEquipment = "Canon EOS C70 Cinema, Sony FE 50mm f/1.2 GM, Amaran 200x Bi-Color",
                projectDescription = "Holiday shopping campaign for broadcast on South TV and digital platforms.",
                estimatedPrice = 35000.0,
                depositAmount = 20000.0,
                paidAmount = 35000.0,
                bookingStatus = "Confirmed",
                paymentStatus = "Fully Paid",
                notes = "Script approved. Direct store manager contact confirmed."
            )
            val b3Id = bookingDao.insertBooking(b3)

            // 6. Initial Payments
            paymentDao.insertPayment(
                PaymentEntity(
                    bookingId = b1Id,
                    receiptNumber = "REC-EVP-00101",
                    customerName = "Pastor Daniel Mekonnen",
                    amount = 45000.0,
                    ethDate = "${EthiopianCalendarHelper.ETH_MONTH_NAMES[todayEth.month - 1]} ${todayEth.day}, ${todayEth.year}",
                    gregDate = "${todayGreg.year}-10-${todayGreg.day}",
                    paymentMethod = "CBE Bank Transfer",
                    referenceNumber = "FT26298ABCD45",
                    paymentStatus = "Fully Paid",
                    notes = "Full project payment for Gospel clip production"
                )
            )

            paymentDao.insertPayment(
                PaymentEntity(
                    bookingId = b2Id,
                    receiptNumber = "REC-EVP-00102",
                    customerName = "Dr. Bethlehem Haile",
                    amount = 25000.0,
                    ethDate = "${EthiopianCalendarHelper.ETH_MONTH_NAMES[todayEth.month - 1]} ${todayEth.day}, ${todayEth.year}",
                    gregDate = "${todayGreg.year}-10-${todayGreg.day}",
                    paymentMethod = "Telebirr",
                    referenceNumber = "TEL892716382",
                    paymentStatus = "Deposit",
                    notes = "50%+ advance deposit for wedding photography & video package"
                )
            )
        }
    }
}
