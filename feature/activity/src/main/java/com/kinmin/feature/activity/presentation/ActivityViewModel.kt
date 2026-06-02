package com.kinmin.feature.activity.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinmin.core.common.Resource
import com.kinmin.feature.activity.domain.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class ActivityUiState(
    val saving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null,
    val photo: File? = null
)

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val repo: ActivityRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ActivityUiState())
    val state = _state.asStateFlow()

    fun setPhoto(file: File) = _state.update { it.copy(photo = file) }

    fun submit(title: String, note: String) = viewModelScope.launch {
        if (title.isBlank()) { _state.update { it.copy(error = "Enter what you did") }; return@launch }
        _state.update { it.copy(saving = true, error = null) }
        when (val r = repo.logActivity(title.trim(), note.ifBlank { null }, _state.value.photo)) {
            is Resource.Success -> _state.update { it.copy(saving = false, saved = true) }
            is Resource.Error -> _state.update { it.copy(saving = false, error = r.message) }
            Resource.Loading -> Unit
        }
    }
}
