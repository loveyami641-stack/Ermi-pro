package com.example.ermipro.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ermipro.data.model.BookingEntity
import com.example.ermipro.data.model.CustomerEntity
import com.example.ermipro.data.model.EquipmentEntity
import com.example.ermipro.data.model.PaymentEntity
import com.example.ermipro.data.model.ServiceEntity
import com.example.ermipro.data.model.StaffEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY ethYear ASC, ethMonth ASC, ethDay ASC, startTime ASC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE ethYear = :year AND ethMonth = :month AND ethDay = :day")
    fun getBookingsForEthDate(year: Int, month: Int, day: Int): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE ethYear = :year AND ethMonth = :month ORDER BY ethDay ASC")
    fun getBookingsForEthMonth(year: Int, month: Int): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE id = :id")
    suspend fun getBookingById(id: Long): BookingEntity?

    @Query("SELECT * FROM bookings WHERE ethYear = :year AND ethMonth = :month AND ethDay = :day AND (assignedCameraOperator = :crew OR assignedPhotographer = :crew OR assignedEditor = :crew) AND bookingStatus != 'Cancelled'")
    suspend fun findCrewConflicts(year: Int, month: Int, day: Int, crew: String): List<BookingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity): Long

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Delete
    suspend fun deleteBooking(booking: BookingEntity)
}

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: Long): CustomerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity): Long

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)

    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity)
}

@Dao
interface ServiceDao {
    @Query("SELECT * FROM services ORDER BY category ASC, title ASC")
    fun getAllServices(): Flow<List<ServiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceEntity): Long

    @Update
    suspend fun updateService(service: ServiceEntity)

    @Delete
    suspend fun deleteService(service: ServiceEntity)
}

@Dao
interface StaffDao {
    @Query("SELECT * FROM staff ORDER BY role ASC, name ASC")
    fun getAllStaff(): Flow<List<StaffEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaff(staff: StaffEntity): Long

    @Update
    suspend fun updateStaff(staff: StaffEntity)

    @Delete
    suspend fun deleteStaff(staff: StaffEntity)
}

@Dao
interface EquipmentDao {
    @Query("SELECT * FROM equipment ORDER BY category ASC, name ASC")
    fun getAllEquipment(): Flow<List<EquipmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipment(equipment: EquipmentEntity): Long

    @Update
    suspend fun updateEquipment(equipment: EquipmentEntity)

    @Delete
    suspend fun deleteEquipment(equipment: EquipmentEntity)
}

@Dao
interface PaymentDao {
    @Query("SELECT * FROM payments ORDER BY createdAt DESC")
    fun getAllPayments(): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE bookingId = :bookingId ORDER BY createdAt DESC")
    fun getPaymentsForBooking(bookingId: Long): Flow<List<PaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity): Long

    @Delete
    suspend fun deletePayment(payment: PaymentEntity)
}
