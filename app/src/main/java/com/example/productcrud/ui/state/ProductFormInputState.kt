package com.example.productcrud.ui.state

import com.example.productcrud.model.ProductCategory
import com.example.productcrud.model.VatRate

data class ProductFormInputState (
    val name: FieldState<String> = FieldState(""),
    val brand: FieldState<String> = FieldState(""),
    val model: FieldState<String> = FieldState(""),
    val netPrice: FieldState<String> = FieldState(""),
    val vatRate: FieldState<VatRate?> = FieldState(null),
    val stockQuantity: FieldState<String> = FieldState(""),
    val category: FieldState<ProductCategory?> = FieldState(null),
    val isActive: Boolean = true
)