package com.example.productcrud.viewModel

import com.example.productcrud.dto.ProductDto
import com.example.productcrud.model.Product

data class ProductUiState(
    val products: List<ProductDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)