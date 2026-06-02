package com.kinmin.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(order: PendingOrderEntity)

    @Query("SELECT * FROM pending_orders WHERE status != 'SYNCED' ORDER BY createdAt ASC")
    suspend fun unsynced(): List<PendingOrderEntity>

    @Query("SELECT * FROM pending_orders WHERE userId = :uid ORDER BY createdAt DESC")
    fun observeForUser(uid: String): Flow<List<PendingOrderEntity>>

    @Query("UPDATE pending_orders SET status = :status, remotePhotoUrl = :url WHERE localId = :id")
    suspend fun updateStatus(id: String, status: String, url: String? = null)
}

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(activity: PendingActivityEntity)

    @Query("SELECT * FROM pending_activities WHERE status != 'SYNCED' ORDER BY createdAt ASC")
    suspend fun unsynced(): List<PendingActivityEntity>

    @Query("UPDATE pending_activities SET status = :status WHERE localId = :id")
    suspend fun updateStatus(id: String, status: String)
}

@Dao
interface RemarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(remark: PendingRemarkEntity)

    @Query("SELECT * FROM pending_remarks WHERE status != 'SYNCED' ORDER BY createdAt ASC")
    suspend fun unsynced(): List<PendingRemarkEntity>

    @Query("UPDATE pending_remarks SET status = :status WHERE localId = :id")
    suspend fun updateStatus(id: String, status: String)
}

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ping: LocationPingEntity)

    @Query("SELECT * FROM location_pings WHERE status != 'SYNCED' ORDER BY recordedAt ASC")
    suspend fun unsynced(): List<LocationPingEntity>

    @Query("UPDATE location_pings SET status = :status WHERE localId = :id")
    suspend fun updateStatus(id: String, status: String)
}
