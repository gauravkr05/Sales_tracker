package com.kinmin.feature.order.data

import com.kinmin.core.common.Resource
import com.kinmin.core.common.safeCall
import com.kinmin.core.database.OrderDao
import com.kinmin.core.database.PendingOrderEntity
import com.kinmin.core.datastore.SessionStore
import com.kinmin.core.sync.SyncScheduler
import com.kinmin.feature.order.domain.OrderRepository
import kotlinx.coroutines.flow.first
import java.io.File
import java.util.UUID
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val session: SessionStore,
    private val syncScheduler: SyncScheduler
) : OrderRepository {

    override suspend fun submitOrder(partyName: String, amount: Double, photo: File?): Resource<Unit> = safeCall {
        val uid = session.userIdFlow.first() ?: error("Not logged in")
        orderDao.upsert(
            PendingOrderEntity(
                localId = UUID.randomUUID().toString(),
                userId = uid,
                partyName = partyName,
                amount = amount,
                photoPath = photo?.absolutePath,
                createdAt = System.currentTimeMillis()
            )
        )
        syncScheduler.requestSync()  // uploads when network is available
        Unit
    }
}
