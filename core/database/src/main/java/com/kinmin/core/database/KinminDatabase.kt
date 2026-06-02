package com.kinmin.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        PendingOrderEntity::class,
        PendingActivityEntity::class,
        PendingRemarkEntity::class,
        LocationPingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KinminDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun activityDao(): ActivityDao
    abstract fun remarkDao(): RemarkDao
    abstract fun locationDao(): LocationDao
}
