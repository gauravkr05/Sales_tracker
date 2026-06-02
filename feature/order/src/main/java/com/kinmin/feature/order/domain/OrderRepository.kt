package com.kinmin.feature.order.domain

import com.kinmin.core.common.Resource
import java.io.File

interface OrderRepository {
    /** Saves an order locally (offline-first) and requests a background sync. */
    suspend fun submitOrder(partyName: String, amount: Double, photo: File?): Resource<Unit>
}
