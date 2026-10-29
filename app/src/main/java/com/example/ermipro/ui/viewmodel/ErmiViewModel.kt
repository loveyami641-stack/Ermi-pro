package com.example.ermipro.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ermipro.calendar.EthDate
import com.example.ermipro.calendar.EthiopianCalendarHelper
import com.example.ermipro.calendar.GregDate
import com.example.ermipro.data.database.ErmiDatabase
import com.example.ermipro.data.model.BookingEntity
import com.example.ermipro.data.model.CustomerEntity
import com.example.ermipro.data.model.EquipmentEntity
import com.example.ermipro.data.model.PaymentEntity
import com.example.ermipro.data.model.ServiceEntity
import com.example.ermipro.data.model.StaffEntity
import com.example.ermipro.data.repository.ErmiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab(val label: String) {
    DASHBOARD("Dashboard"),
    CALENDAR("Calendar"),
    BOOKINGS("Bookings"),
    CUSTOMERS("Customers"),
    MORE("More")
}

enum class MoreSubView(val label: String) {
    NONE("Menu"),
    SERVICES("Services"),
    PAYMENTS("Payments"),
    STAFF("Staff & Crew"),
    EQUIPMENT("Equipment"),
    REPORTS("Reports & Analytics"),
    SETTINGS("Settings")
}

enum class CalendarViewMode {
    MONTH, WEEK, DAY, AGENDA
}

enum class UserRole(val label: String) {
    ADMINISTRATOR("Administrator"),
    MANAGER("Manager"),
    STAFF("Staff Crew")
}

data class ConflictWarning(
    val hasConflict: Boolean,
    val conflictingBooking: BookingEntity?,
    val conflictingEntityName: String
)

class ErmiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ErmiRepository

    init {
        val db = ErmiDatabase.getDatabase(application, viewModelScope)
        repository = ErmiRepository(
            bookingDao = db.bookingDao(),
            customerDao = db.customerDao(),
            serviceDao = db.serviceDao(),
            staffDao = db.staffDao(),
            equipmentDao = db.equipmentDao(),
            paymentDao = db.paymentDao()
        )
    }

    // Active Navigation
    private val _currentTab = MutableStateFlow(MainTab.DASHBOARD)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _currentMoreSubView = MutableStateFlow(MoreSubView.NONE)
    val currentMoreSubView: StateFlow<MoreSubView> = _currentMoreSubView.asStateFlow()

    // User Role
    private val _currentRole = MutableStateFlow(UserRole.ADMINISTRATOR)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    // Ethiopian Calendar Selection
    private val _selectedEthDate = MutableStateFlow(EthiopianCalendarHelper.getTodayEth())
    val selectedEthDate: StateFlow<EthDate> = _selectedEthDate.asStateFlow()

    private val _calendarViewMode = MutableStateFlow(CalendarViewMode.MONTH)
    val calendarViewMode: StateFlow<CalendarViewMode> = _calendarViewMode.asStateFlow()

    private val _showAmharicCalendar = MutableStateFlow(false)
    val showAmharicCalendar: StateFlow<Boolean> = _showAmharicCalendar.asStateFlow()

    private val _primaryCalendarIsEth = MutableStateFlow(true)
    val primaryCalendarIsEth: StateFlow<Boolean> = _primaryCalendarIsEth.asStateFlow()

    // Search and Filters
    private val _bookingSearchQuery = MutableStateFlow("")
    val bookingSearchQuery: StateFlow<String> = _bookingSearchQuery.asStateFlow()

    private val _bookingStatusFilter = MutableStateFlow("All")
    val bookingStatusFilter: StateFlow<String> = _bookingStatusFilter.asStateFlow()

    private val _customerSearchQuery = MutableStateFlow("")
    val customerSearchQuery: StateFlow<String> = _customerSearchQuery.asStateFlow()

    // Repositories Data
    val bookings: StateFlow<List<BookingEntity>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val customers: StateFlow<List<CustomerEntity>> = repository.allCustomers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val services: StateFlow<List<ServiceEntity>> = repository.allServices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val staff: StateFlow<List<StaffEntity>> = repository.allStaff
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val equipment: StateFlow<List<EquipmentEntity>> = repository.allEquipment
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val payments: StateFlow<List<PaymentEntity>> = repository.allPayments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Bookings
    val filteredBookings: StateFlow<List<BookingEntity>> = combine(
        bookings,
        _bookingSearchQuery,
        _bookingStatusFilter
    ) { list, query, filter ->
        val todayEth = EthiopianCalendarHelper.getTodayEth()
        list.filter { b ->
            val matchesQuery = query.isBlank() ||
                    b.customerName.contains(query, ignoreCase = true) ||
                    b.bookingNumber.contains(query, ignoreCase = true) ||
                    b.serviceTitle.contains(query, ignoreCase = true) ||
                    b.serviceCategory.contains(query, ignoreCase = true) ||
                    b.customerPhone.contains(query, ignoreCase = true) ||
                    b.location.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                "All" -> true
                "Today" -> b.ethYear == todayEth.year && b.ethMonth == todayEth.month && b.ethDay == todayEth.day
                "Upcoming" -> (b.ethYear > todayEth.year) ||
                        (b.ethYear == todayEth.year && b.ethMonth > todayEth.month) ||
                        (b.ethYear == todayEth.year && b.ethMonth == todayEth.month && b.ethDay >= todayEth.day)
                else -> b.bookingStatus.equals(filter, ignoreCase = true)
            }
            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Dialog & Inspection States
    private val _inspectedBooking = MutableStateFlow<BookingEntity?>(null)
    val inspectedBooking: StateFlow<BookingEntity?> = _inspectedBooking.asStateFlow()

    private val _inspectedReceipt = MutableStateFlow<PaymentEntity?>(null)
    val inspectedReceipt: StateFlow<PaymentEntity?> = _inspectedReceipt.asStateFlow()

    private val _isAddBookingOpen = MutableStateFlow(false)
    val isAddBookingOpen: StateFlow<Boolean> = _isAddBookingOpen.asStateFlow()

    private val _bookingToEdit = MutableStateFlow<BookingEntity?>(null)
    val bookingToEdit: StateFlow<BookingEntity?> = _bookingToEdit.asStateFlow()

    private val _isAddCustomerOpen = MutableStateFlow(false)
    val isAddCustomerOpen: StateFlow<Boolean> = _isAddCustomerOpen.asStateFlow()

    private val _isAddPaymentOpen = MutableStateFlow(false)
    val isAddPaymentOpen: StateFlow<Boolean> = _isAddPaymentOpen.asStateFlow()

    private val _isAddServiceOpen = MutableStateFlow(false)
    val isAddServiceOpen: StateFlow<Boolean> = _isAddServiceOpen.asStateFlow()

    private val _isAddStaffOpen = MutableStateFlow(false)
    val isAddStaffOpen: StateFlow<Boolean> = _isAddStaffOpen.asStateFlow()

    private val _isAddEquipmentOpen = MutableStateFlow(false)
    val isAddEquipmentOpen: StateFlow<Boolean> = _isAddEquipmentOpen.asStateFlow()

    // Navigation setters
    fun setTab(tab: MainTab) {
        _currentTab.value = tab
        if (tab != MainTab.MORE) {
            _currentMoreSubView.value = MoreSubView.NONE
        }
    }

    fun setMoreSubView(subView: MoreSubView) {
        _currentMoreSubView.value = subView
    }

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun setSelectedEthDate(date: EthDate) {
        _selectedEthDate.value = date
    }

    fun navigateMonth(delta: Int) {
        val current = _selectedEthDate.value
        var newMonth = current.month + delta
        var newYear = current.year
        if (newMonth > 13) {
            newMonth = 1
            newYear += 1
        } else if (newMonth < 1) {
            newMonth = 13
            newYear -= 1
        }
        val maxDays = EthiopianCalendarHelper.getDaysInEthMonth(newYear, newMonth)
        val newDay = current.day.coerceAtMost(maxDays)
        _selectedEthDate.value = EthDate(newYear, newMonth, newDay)
    }

    fun setCalendarViewMode(mode: CalendarViewMode) {
        _calendarViewMode.value = mode
    }

    fun toggleAmharicCalendar() {
        _showAmharicCalendar.value = !_showAmharicCalendar.value
    }

    fun togglePrimaryCalendar() {
        _primaryCalendarIsEth.value = !_primaryCalendarIsEth.value
    }

    fun setBookingSearchQuery(q: String) {
        _bookingSearchQuery.value = q
    }

    fun setBookingStatusFilter(f: String) {
        _bookingStatusFilter.value = f
    }

    fun setCustomerSearchQuery(q: String) {
        _customerSearchQuery.value = q
    }

    fun inspectBooking(b: BookingEntity?) {
        _inspectedBooking.value = b
    }

    fun inspectReceipt(p: PaymentEntity?) {
        _inspectedReceipt.value = p
    }

    fun openAddBooking(presetEthDate: EthDate? = null) {
        if (presetEthDate != null) {
            _selectedEthDate.value = presetEthDate
        }
        _bookingToEdit.value = null
        _isAddBookingOpen.value = true
    }

    fun openEditBooking(b: BookingEntity) {
        _bookingToEdit.value = b
        _isAddBookingOpen.value = true
    }

    fun closeAddBooking() {
        _isAddBookingOpen.value = false
        _bookingToEdit.value = null
    }

    fun openAddCustomer() {
        _isAddCustomerOpen.value = true
    }

    fun closeAddCustomer() {
        _isAddCustomerOpen.value = false
    }

    fun openAddPayment() {
        _isAddPaymentOpen.value = true
    }

    fun closeAddPayment() {
        _isAddPaymentOpen.value = false
    }

    fun openAddService() {
        _isAddServiceOpen.value = true
    }

    fun closeAddService() {
        _isAddServiceOpen.value = false
    }

    fun openAddStaff() {
        _isAddStaffOpen.value = true
    }

    fun closeAddStaff() {
        _isAddStaffOpen.value = false
    }

    fun openAddEquipment() {
        _isAddEquipmentOpen.value = true
    }

    fun closeAddEquipment() {
        _isAddEquipmentOpen.value = false
    }

    // Double-booking check
    fun checkCrewOrEquipmentConflict(
        ethYear: Int,
        ethMonth: Int,
        ethDay: Int,
        crew: String,
        equipment: String,
        excludeBookingId: Long = 0
    ): ConflictWarning {
        val existing = bookings.value.filter {
            it.id != excludeBookingId &&
                    it.ethYear == ethYear &&
                    it.ethMonth == ethMonth &&
                    it.ethDay == ethDay &&
                    it.bookingStatus != "Cancelled"
        }

        if (crew.isNotBlank()) {
            val conflict = existing.firstOrNull {
                it.assignedCameraOperator.equals(crew, ignoreCase = true) ||
                        it.assignedPhotographer.equals(crew, ignoreCase = true) ||
                        it.assignedEditor.equals(crew, ignoreCase = true)
            }
            if (conflict != null) {
                return ConflictWarning(true, conflict, crew)
            }
        }

        if (equipment.isNotBlank()) {
            val conflict = existing.firstOrNull {
                it.assignedEquipment.contains(equipment, ignoreCase = true)
            }
            if (conflict != null) {
                return ConflictWarning(true, conflict, equipment)
            }
        }

        return ConflictWarning(false, null, "")
    }

    // CRUD Actions
    fun saveBooking(booking: BookingEntity) {
        viewModelScope.launch {
            if (booking.id == 0L) {
                repository.insertBooking(booking)
            } else {
                repository.updateBooking(booking)
            }
            closeAddBooking()
            _inspectedBooking.value = booking
        }
    }

    fun updateBookingStatus(booking: BookingEntity, newStatus: String) {
        viewModelScope.launch {
            val updated = booking.copy(bookingStatus = newStatus)
            repository.updateBooking(updated)
            _inspectedBooking.value = updated
        }
    }

    fun deleteBooking(booking: BookingEntity) {
        viewModelScope.launch {
            repository.deleteBooking(booking)
            _inspectedBooking.value = null
        }
    }

    fun saveCustomer(customer: CustomerEntity) {
        viewModelScope.launch {
            if (customer.id == 0L) {
                repository.insertCustomer(customer)
            } else {
                repository.updateCustomer(customer)
            }
            closeAddCustomer()
        }
    }

    fun deleteCustomer(customer: CustomerEntity) {
        viewModelScope.launch {
            repository.deleteCustomer(customer)
        }
    }

    fun recordPayment(payment: PaymentEntity) {
        viewModelScope.launch {
            repository.insertPayment(payment)
            // Also update associated booking's paid amount & payment status
            val booking = repository.getBookingById(payment.bookingId)
            if (booking != null) {
                val newPaid = booking.paidAmount + payment.amount
                val newStatus = when {
                    newPaid >= booking.estimatedPrice -> "Fully Paid"
                    newPaid > 0 -> "Partially Paid"
                    else -> "Unpaid"
                }
                repository.updateBooking(booking.copy(paidAmount = newPaid, paymentStatus = newStatus))
            }
            closeAddPayment()
            _inspectedReceipt.value = payment
        }
    }

    fun saveService(service: ServiceEntity) {
        viewModelScope.launch {
            if (service.id == 0L) {
                repository.insertService(service)
            } else {
                repository.updateService(service)
            }
            closeAddService()
        }
    }

    fun deleteService(service: ServiceEntity) {
        viewModelScope.launch {
            repository.deleteService(service)
        }
    }

    fun saveStaff(staffMember: StaffEntity) {
        viewModelScope.launch {
            if (staffMember.id == 0L) {
                repository.insertStaff(staffMember)
            } else {
                repository.updateStaff(staffMember)
            }
            closeAddStaff()
        }
    }

    fun deleteStaff(staffMember: StaffEntity) {
        viewModelScope.launch {
            repository.deleteStaff(staffMember)
        }
    }

    fun saveEquipment(item: EquipmentEntity) {
        viewModelScope.launch {
            if (item.id == 0L) {
                repository.insertEquipment(item)
            } else {
                repository.updateEquipment(item)
            }
            closeAddEquipment()
        }
    }

    fun deleteEquipment(item: EquipmentEntity) {
        viewModelScope.launch {
            repository.deleteEquipment(item)
        }
    }
}
