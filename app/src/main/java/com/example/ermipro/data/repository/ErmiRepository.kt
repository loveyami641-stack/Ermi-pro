package com.example.ermipro.data.repository

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
import kotlinx.coroutines.flow.Flow

class ErmiRepository(
    private val bookingDao: BookingDao,
    private val customerDao: CustomerDao,
    private val serviceDao: ServiceDao,
    private val staffDao: StaffDao,
    private val equipmentDao: EquipmentDao,
    private val paymentDao: PaymentDao
) {
    // Bookings
    val allBookings: Flow<List<BookingEntity>> = bookingDao.getAllBookings()

    fun getBookingsForEthDate(year: Int, month: Int, day: Int): Flow<List<BookingEntity>> =
        bookingDao.getBookingsForEthDate(year, month, day)

    fun getBookingsForEthMonth(year: Int, month: Int): Flow<List<BookingEntity>> =
        bookingDao.getBookingsForEthMonth(year, month)

    suspend fun getBookingById(id: Long): BookingEntity? = bookingDao.getBookingById(id)

    suspend fun findCrewConflicts(year: Int, month: Int, day: Int, crew: String): List<BookingEntity> =
        bookingDao.findCrewConflicts(year, month, day, crew)

    suspend fun insertBooking(booking: BookingEntity): Long = bookingDao.insertBooking(booking)

    suspend fun updateBooking(booking: BookingEntity) = bookingDao.updateBooking(booking)

    suspend fun deleteBooking(booking: BookingEntity) = bookingDao.deleteBooking(booking)

    // Customers
    val allCustomers: Flow<List<CustomerEntity>> = customerDao.getAllCustomers()

    suspend fun getCustomerById(id: Long): CustomerEntity? = customerDao.getCustomerById(id)

    suspend fun insertCustomer(customer: CustomerEntity): Long = customerDao.insertCustomer(customer)

    suspend fun updateCustomer(customer: CustomerEntity) = customerDao.updateCustomer(customer)

    suspend fun deleteCustomer(customer: CustomerEntity) = customerDao.deleteCustomer(customer)

    // Services
    val allServices: Flow<List<ServiceEntity>> = serviceDao.getAllServices()

    suspend fun insertService(service: ServiceEntity): Long = serviceDao.insertService(service)

    suspend fun updateService(service: ServiceEntity) = serviceDao.updateService(service)

    suspend fun deleteService(service: ServiceEntity) = serviceDao.deleteService(service)

    // Staff
    val allStaff: Flow<List<StaffEntity>> = staffDao.getAllStaff()

    suspend fun insertStaff(staff: StaffEntity): Long = staffDao.insertStaff(staff)

    suspend fun updateStaff(staff: StaffEntity) = staffDao.updateStaff(staff)

    suspend fun deleteStaff(staff: StaffEntity) = staffDao.deleteStaff(staff)

    // Equipment
    val allEquipment: Flow<List<EquipmentEntity>> = equipmentDao.getAllEquipment()

    suspend fun insertEquipment(equipment: EquipmentEntity): Long = equipmentDao.insertEquipment(equipment)

    suspend fun updateEquipment(equipment: EquipmentEntity) = equipmentDao.updateEquipment(equipment)

    suspend fun deleteEquipment(equipment: EquipmentEntity) = equipmentDao.deleteEquipment(equipment)

    // Payments
    val allPayments: Flow<List<PaymentEntity>> = paymentDao.getAllPayments()

    fun getPaymentsForBooking(bookingId: Long): Flow<List<PaymentEntity>> =
        paymentDao.getPaymentsForBooking(bookingId)

    suspend fun insertPayment(payment: PaymentEntity): Long = paymentDao.insertPayment(payment)

    suspend fun deletePayment(payment: PaymentEntity) = paymentDao.deletePayment(payment)
}
