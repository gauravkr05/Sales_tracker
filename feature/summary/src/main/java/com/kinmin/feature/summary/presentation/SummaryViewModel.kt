package com.kinmin.feature.summary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinmin.core.database.OrderDao
import com.kinmin.core.datastore.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class SummaryUiState(
    val orderCount: Int = 0,
    val totalValue: Double = 0.0,
    val pending: Int = 0
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SummaryViewModel @Inject constructor(
    session: SessionStore,
    orderDao: OrderDao
) : ViewModel() {

    val state = session.userIdFlow.filterNotNull()
        .flatMapLatest { uid -> orderDao.observeForUser(uid) }
        .map { orders ->
            SummaryUiState(
                orderCount = orders.size,
                totalValue = orders.sumOf { it.amount },
                pending = orders.count { it.status != "SYNCED" }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SummaryUiState())
}
