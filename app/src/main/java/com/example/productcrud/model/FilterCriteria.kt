package com.example.productcrud.model

data class FilterCriteria(
    val category: ProductCategory? = null,
    val isActive: Boolean? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val minStock: Int? = null,
    val maxStock: Int? = null,
    val query: String? = null
)