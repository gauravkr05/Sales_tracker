package com.kinmin.core.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kinmin.core.database.ActivityDao
import com.kinmin.core.database.LocationDao
import com.kinmin.core.database.OrderDao
import com.kinmin.core.database.RemarkDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import java.io.File

/**
 * Flushes every PENDING row to Supabase: photos -> Storage, rows -> Postgres.
 * Runs only with network (constraint set by SyncScheduler). Failures stay
 * PENDING/FAILED so the next pass retries them. This is what makes the app
 * offline-first: the UI never waits on the network.
 */
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val supabase: SupabaseClient,
    private val orderDao: OrderDao,
    private val activityDao: ActivityDao,
    private val remarkDao: RemarkDao,
    private val locationDao: LocationDao
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            syncOrders()
            syncActivities()
            syncRemarks()
            syncLocations()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private suspend fun uploadPhoto(path: String?): String? {
        if (path.isNullOrBlank()) return null
        val file = File(path)
        if (!file.exists()) return null
        val key = "photos/${file.name}"
        supabase.storage.from("kinmin-photos").upload(key, file.readBytes()) { upsert = true }
        // Public URL (bucket can be public, or use createSignedUrl for private buckets)
        return supabase.storage.from("kinmin-photos").publicUrl(key)
    }

    private suspend fun syncOrders() {
        orderDao.unsynced().forEach { o ->
            orderDao.updateStatus(o.localId, "SYNCING")
            val url = uploadPhoto(o.photoPath)
            supabase.postgrest.from("orders").insert(
                OrderDto(o.userId, o.partyName, o.amount, url, o.createdAt)
            )
            orderDao.updateStatus(o.localId, "SYNCED", url)
        }
    }

    private suspend fun syncActivities() {
        activityDao.unsynced().forEach { a ->
            activityDao.updateStatus(a.localId, "SYNCING")
            val url = uploadPhoto(a.photoPath)
            supabase.postgrest.from("activities").insert(
                ActivityDto(a.userId, a.title, a.note, url, a.createdAt)
            )
            activityDao.updateStatus(a.localId, "SYNCED")
        }
    }

    private suspend fun syncRemarks() {
        remarkDao.unsynced().forEach { r ->
            supabase.postgrest.from("remarks").insert(
                RemarkDto(r.userId, r.text, r.createdAt)
            )
            remarkDao.updateStatus(r.localId, "SYNCED")
        }
    }

    private suspend fun syncLocations() {
        locationDao.unsynced().forEach { p ->
            supabase.postgrest.from("location_pings").insert(
                LocationPingDto(p.userId, p.lat, p.lng, p.recordedAt)
            )
            locationDao.updateStatus(p.localId, "SYNCED")
        }
    }
}
