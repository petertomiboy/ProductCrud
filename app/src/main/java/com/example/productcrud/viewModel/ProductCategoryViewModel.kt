package com.example.productcrud.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productcrud.data.ProductCategoryRepository
import com.example.productcrud.model.ProductCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductCategoryViewModel @Inject constructor(
    private val repository: ProductCategoryRepository
) : ViewModel() {

    val categories = repository.categories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun addCategoryIfNotExists(name: String): Boolean {
        val exists = categories.value.any { it.name.equals(name, ignoreCase = true) }
        if (!exists) {
            viewModelScope.launch { repository.addCategory(name) }
        }
        return !exists
    }

    fun updateCategory(category: ProductCategory) {
        viewModelScope.launch { repository.updateCategory(category) }
    }

    fun softDeleteCategory(id: Int) {
        viewModelScope.launch { repository.softDeleteCategory(id) }
    }
}