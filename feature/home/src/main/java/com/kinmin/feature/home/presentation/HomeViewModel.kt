package com.kinmin.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinmin.core.datastore.SessionStore
import com.kinmin.core.location.LocationController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val name: String = "",
    val checkedIn: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val session: SessionStore,
    private val locationController: LocationController
) : ViewModel() {

    val state = combine(session.userNameFlow, session.checkedInFlow) { name, checked ->
        HomeUiState(name = name ?: "there", checkedIn = checked)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    /** Toggles check-in: starts/stops the 5-min location tracking service. */
    fun toggleCheckIn(currentlyIn: Boolean) = viewModelScope.launch {
        if (currentlyIn) {
            locationController.stop()
            session.setCheckedIn(false)
        } else {
            locationController.start()
            session.setCheckedIn(true)
        }
    }
}
