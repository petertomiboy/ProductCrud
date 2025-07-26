package com.example.productcrud.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productcrud.data.IProductRepository
import com.example.productcrud.dto.ProductDto
import com.example.productcrud.model.FilterCriteria
import com.example.productcrud.model.ProductCategory
import com.example.productcrud.model.VatRate
import com.example.productcrud.utils.import.ProductJsonImport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: IProductRepository
): ViewModel() {

    private val _state = MutableStateFlow(ProductUiState())
    val state: StateFlow<ProductUiState> = _state.asStateFlow()

    private val _categories = MutableStateFlow<List<ProductCategory>>(emptyList())
    val categories: StateFlow<List<ProductCategory>> = _categories.asStateFlow()

    private val _vatRates = MutableStateFlow<List<VatRate>>(emptyList())
    val vatRates: StateFlow<List<VatRate>> = _vatRates.asStateFlow()

    private val _filterCriteria = MutableStateFlow<FilterCriteria?>(null)
    val filterCriteria: StateFlow<FilterCriteria?> = _filterCriteria.asStateFlow()

    init {
        viewModelScope.launch {
            repository.populateDefaultsIfNeeded()
        }
        loadProducts()
        loadCategories()
        loadVatRates()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getAllCategories()
                .catch { e -> _state.update { it.copy(error = e.message) } }
                .collect { _categories.value = it }
        }
    }

    private fun loadVatRates() {
        viewModelScope.launch {
            repository.getAllVatRates()
                .catch { e -> _state.update { it.copy(error = e.message) } }
                .collect { _vatRates.value = it }
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            repository.getAllProducts()
                .onStart { _state.update { it.copy(isLoading = true) } }
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { productList ->
                    _state.update {
                        it.copy(products = productList, isLoading = false, error = null)
                    }
                }
        }
    }

    fun deleteProduct(productDto: ProductDto) {
        viewModelScope.launch {
            try {
                repository.deleteProduct(productDto)
                loadProducts()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun insertProduct(productDto: ProductDto) {
        viewModelScope.launch {
            try {
                repository.insertProduct(productDto)
                loadProducts()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun updateProduct(productDto: ProductDto) {
        viewModelScope.launch {
            try {
                repository.updateProduct(productDto)
                loadProducts()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun applyFilters(criteria: FilterCriteria) {
        _filterCriteria.value = criteria

        viewModelScope.launch {
            repository.getAllProducts()
                .collect { products ->
                    val filtered = products.filter { product ->
                        val categoryMatch = criteria.category?.name?.let { it == product.categoryName } ?: true
                        val statusMatch = criteria.isActive?.let { it == product.isActive } ?: true
                        val minPriceMatch = criteria.minPrice?.let { product.netPrice >= it } ?: true
                        val maxPriceMatch = criteria.maxPrice?.let { product.netPrice <= it } ?: true
                        val minStockMatch = criteria.minStock?.let { product.stock >= it } ?: true
                        val maxStockMatch = criteria.maxStock?.let { product.stock <= it } ?: true
                        val queryMatch = criteria.query?.let {
                            product.name.contains(it, true)
                                    || product.brand.contains(it, true)
                                    || product.model.contains(it, true)
                        } ?: true

                        categoryMatch && statusMatch && minPriceMatch && maxPriceMatch && minStockMatch && maxStockMatch && queryMatch
                    }

                    _state.update { it.copy(products = filtered) }
                }
        }
    }

    fun importProductsFromJson(context: Context) {
        viewModelScope.launch {
            try {
                val products = ProductJsonImport.importFromJson(context)
                products.forEach { product ->
                    repository.insertProduct(product) // vagy update, ahogy kell
                }
            } catch (e: Exception) {
                // Kezeld a hibát pl. SnackBar-ral
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}