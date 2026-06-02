package com.kinmin.feature.remarks.data

import com.kinmin.core.common.Resource
import com.kinmin.core.common.safeCall
import com.kinmin.core.database.PendingRemarkEntity
import com.kinmin.core.database.RemarkDao
import com.kinmin.core.datastore.SessionStore
import com.kinmin.core.sync.SyncScheduler
import com.kinmin.feature.remarks.domain.RemarkRepository
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class RemarkRepositoryImpl @Inject constructor(
    private val remarkDao: RemarkDao,
    private val session: SessionStore,
    private val syncScheduler: SyncScheduler
) : RemarkRepository {
    override suspend fun addRemark(text: String): Resource<Unit> = safeCall {
        val uid = session.userIdFlow.first() ?: error("Not logged in")
        remarkDao.upsert(
            PendingRemarkEntity(
                localId = UUID.randomUUID().toString(),
                userId = uid,
                text = text,
                createdAt = System.currentTimeMillis()
            )
        )
        syncScheduler.requestSync()
        Unit
    }
}
