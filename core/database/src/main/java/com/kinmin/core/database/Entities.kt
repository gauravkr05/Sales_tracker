package com.kinmin.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Offline-first queue tables. Everything the salesperson does is written here
 * FIRST (status = PENDING), then the SyncWorker uploads and flips it to SYNCED.
 * status values: PENDING, SYNCING, SYNCED, FAILED
 */

@Entity(tableName = "pending_orders")
data class PendingOrderEntity(
    @PrimaryKey val localId: String,
    val userId: String,
    val partyName: String,
    val amount: Double,
    val photoPath: String?,      // local file path until uploaded
    val createdAt: Long,
    val status: String = "PENDING",
    val remotePhotoUrl: String? = null
)

@Entity(tableName = "pending_activities")
data class PendingActivityEntity(
    @PrimaryKey val localId: String,
    val userId: String,
    val title: String,
    val note: String?,
    val photoPath: String?,
    val createdAt: Long,
    val status: String = "PENDING"
)

@Entity(tableName = "pending_remarks")
data class PendingRemarkEntity(
    @PrimaryKey val localId: String,
    val userId: String,
    val text: String,
    val createdAt: Long,
    val status: String = "PENDING"
)

@Entity(tableName = "location_pings")
data class LocationPingEntity(
    @PrimaryKey val localId: String,
    val userId: String,
    val lat: Double,
    val lng: Double,
    val recordedAt: Long,
    val status: String = "PENDING"
)
