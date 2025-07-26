package com.example.productcrud.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productcrud.data.VatRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VatRateViewModel @Inject constructor(
    private val repository: VatRateRepository
) : ViewModel() {

    val vatRates = repository.vatRates.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun softDeleteVatRate(id: Int) {
        viewModelScope.launch {
            repository.softDeleteVatRate(id)
        }
    }

    fun addVatRateIfNotExists(percentage: Int):Boolean {
        val exists = vatRates.value.any { it.percentage == percentage }
        if (!exists){
            viewModelScope.launch {
                repository.addVatRate(percentage)
            }
        }
        return !exists //true, ha hozzáadta, false, ha már létezett
    }
}