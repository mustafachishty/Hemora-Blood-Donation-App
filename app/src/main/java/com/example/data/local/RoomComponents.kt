package com.example.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "cached_blood_requests")
data class CachedBloodRequest(
    @PrimaryKey val id: String,
    val patientName: String,
    val bloodGroup: String,
    val unitsRequired: Int,
    val hospitalName: String,
    val hospitalLat: Double,
    val hospitalLng: Double,
    val contactNumber: String,
    val urgencyLevel: String,
    val notes: String,
    val timestamp: Long,
    val expiresAt: Long,
    val requesterId: String,
    val requesterName: String,
    val isFulfilled: Boolean
)

@Entity(tableName = "cached_notifications")
data class CachedNotification(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean,
    val type: String
)

@Dao
interface BloodRequestDao {
    @Query("SELECT * FROM cached_blood_requests ORDER BY timestamp DESC")
    fun getAllRequests(): Flow<List<CachedBloodRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: CachedBloodRequest)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRequests(requests: List<CachedBloodRequest>)

    @Query("UPDATE cached_blood_requests SET isFulfilled = :fulfilled WHERE id = :id")
    suspend fun updateFulfillment(id: String, fulfilled: Boolean)

    @Query("DELETE FROM cached_blood_requests WHERE id = :id")
    suspend fun deleteRequestById(id: String)

    @Query("DELETE FROM cached_blood_requests")
    suspend fun clearAll()
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM cached_notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<CachedNotification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: CachedNotification)

    @Query("UPDATE cached_notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("DELETE FROM cached_notifications WHERE id = :id")
    suspend fun deleteNotificationById(id: String)
}

@Database(entities = [CachedBloodRequest::class, CachedNotification::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bloodRequestDao(): BloodRequestDao
    abstract fun notificationDao(): NotificationDao
}
