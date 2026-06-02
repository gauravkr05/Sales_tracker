package com.kinmin.feature.remarks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinmin.core.common.Resource
import com.kinmin.feature.remarks.domain.RemarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RemarksUiState(val saving: Boolean = false, val saved: Boolean = false, val error: String? = null)

@HiltViewModel
class RemarksViewModel @Inject constructor(
    private val repo: RemarkRepository
) : ViewModel() {
    private val _state = MutableStateFlow(RemarksUiState())
    val state = _state.asStateFlow()

    fun submit(text: String) = viewModelScope.launch {
        if (text.isBlank()) { _state.update { it.copy(error = "Write a remark first") }; return@launch }
        _state.update { it.copy(saving = true, error = null) }
        when (val r = repo.addRemark(text.trim())) {
            is Resource.Success -> _state.update { it.copy(saving = false, saved = true) }
            is Resource.Error -> _state.update { it.copy(saving = false, error = r.message) }
            Resource.Loading -> Unit
        }
    }
}
