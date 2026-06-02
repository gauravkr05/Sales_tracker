package com.kinmin.core.location

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kinmin.core.database.LocationDao
import com.kinmin.core.database.LocationPingEntity
import com.kinmin.core.datastore.SessionStore
import com.kinmin.core.sync.SyncScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * Foreground service that records a GPS ping every 5 minutes between
 * check-in and check-out. Each ping is written to Room (offline-safe) and
 * a sync pass is requested so it reaches Supabase when there is network.
 */
@AndroidEntryPoint
class LocationTrackingService : Service() {

    @Inject lateinit var locationDao: LocationDao
    @Inject lateinit var session: SessionStore
    @Inject lateinit var syncScheduler: SyncScheduler

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val client by lazy { LocationServices.getFusedLocationProviderClient(this) }

    private val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val loc = result.lastLocation ?: return
            scope.launch {
                val uid = session.userIdFlow.first() ?: return@launch
                locationDao.insert(
                    LocationPingEntity(
                        localId = UUID.randomUUID().toString(),
                        userId = uid,
                        lat = loc.latitude,
                        lng = loc.longitude,
                        recordedAt = System.currentTimeMillis()
                    )
                )
                syncScheduler.requestSync()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIF_ID, buildNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        requestUpdates()
        return START_STICKY
    }

    private fun requestUpdates() {
        val fineGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!fineGranted) { stopSelf(); return }

        val request = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, FIVE_MIN)
            .setMinUpdateIntervalMillis(FIVE_MIN)
            .build()
        client.requestLocationUpdates(request, callback, mainLooper)
    }

    private fun buildNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Field tracking", NotificationManager.IMPORTANCE_LOW
            )
            (getSystemService(NotificationManager::class.java)).createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Kinmin Tracker")
            .setContentText("Tracking active until check-out")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        client.removeLocationUpdates(callback)
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val CHANNEL_ID = "kinmin_tracking"
        private const val NOTIF_ID = 4201
        private const val FIVE_MIN = 5 * 60 * 1000L
    }
}
