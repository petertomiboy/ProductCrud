package com.example.productcrud.dto

data class ProductDto (
    val id: Int = 0,
    val name: String,
    val brand: String,
    val model: String,
    val netPrice: Double,
    val vatValue: Int,             // ÁFA százalék (pl. 27)
    val stock: Int,
    val categoryName: String,      // kategória neve (pl. "Notebook")
    val isActive: Boolean
)