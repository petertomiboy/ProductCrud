package com.example.productcrud.data

import com.example.productcrud.dto.ProductDto
import com.example.productcrud.model.Product
import com.example.productcrud.model.ProductCategory
import com.example.productcrud.model.VatRate
import kotlinx.coroutines.flow.Flow

interface IProductRepository {
    fun getAllProducts(): Flow<List<ProductDto>>
    suspend fun insertProduct(product: ProductDto)
    suspend fun updateProduct(product: ProductDto)
    suspend fun deleteProduct(product: ProductDto)
    suspend fun getProductById(id: Int): ProductDto?
    fun getAllCategories(): Flow<List<ProductCategory>>
    fun getAllVatRates(): Flow<List<VatRate>>
    suspend fun populateDefaultsIfNeeded()
}

