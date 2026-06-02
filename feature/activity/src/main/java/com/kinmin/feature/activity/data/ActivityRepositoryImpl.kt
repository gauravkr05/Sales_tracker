package com.kinmin.feature.activity.data

import com.kinmin.core.common.Resource
import com.kinmin.core.common.safeCall
import com.kinmin.core.database.ActivityDao
import com.kinmin.core.database.PendingActivityEntity
import com.kinmin.core.datastore.SessionStore
import com.kinmin.core.sync.SyncScheduler
import com.kinmin.feature.activity.domain.ActivityRepository
import kotlinx.coroutines.flow.first
import java.io.File
import java.util.UUID
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val activityDao: ActivityDao,
    private val session: SessionStore,
    private val syncScheduler: SyncScheduler
) : ActivityRepository {
    override suspend fun logActivity(title: String, note: String?, photo: File?): Resource<Unit> = safeCall {
        val uid = session.userIdFlow.first() ?: error("Not logged in")
        activityDao.upsert(
            PendingActivityEntity(
                localId = UUID.randomUUID().toString(),
                userId = uid,
                title = title,
                note = note,
                photoPath = photo?.absolutePath,
                createdAt = System.currentTimeMillis()
            )
        )
        syncScheduler.requestSync()
        Unit
    }
}
