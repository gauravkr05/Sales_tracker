package com.kinmin.feature.order.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinmin.core.common.Resource
import com.kinmin.feature.order.domain.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class OrderUiState(
    val saving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null,
    val photo: File? = null
)

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repo: OrderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OrderUiState())
    val state = _state.asStateFlow()

    fun setPhoto(file: File) = _state.update { it.copy(photo = file) }

    fun submit(partyName: String, amountText: String) = viewModelScope.launch {
        val amount = amountText.toDoubleOrNull()
        if (partyName.isBlank() || amount == null) {
            _state.update { it.copy(error = "Enter a party name and a valid amount") }
            return@launch
        }
        _state.update { it.copy(saving = true, error = null) }
        when (val res = repo.submitOrder(partyName.trim(), amount, _state.value.photo)) {
            is Resource.Success -> _state.update { it.copy(saving = false, saved = true) }
            is Resource.Error -> _state.update { it.copy(saving = false, error = res.message) }
            Resource.Loading -> Unit
        }
    }
}
