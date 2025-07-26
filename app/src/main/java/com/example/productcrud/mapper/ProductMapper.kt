package com.example.productcrud.mapper

import android.health.connect.datatypes.units.Percentage
import com.example.productcrud.dto.ProductDto
import com.example.productcrud.model.Product
import com.example.productcrud.model.ProductCategory
import com.example.productcrud.model.VatRate

fun Product.toDto(
    categoryName: String,
    vatPercentage: Int
): ProductDto {
    return ProductDto(
        id = this.id,
        name = this.name,
        brand = this.brand,
        model = this.model,
        netPrice = this.netPrice,
        vatValue = vatPercentage,
        stock = this.stock,
        categoryName = categoryName,
        isActive = this.isActive
    )
}

fun ProductDto.toEntity(
    categoryId: Int?,
    vatId: Int
): Product {
    return Product(
        id = this.id,
        name = this.name,
        brand = this.brand,
        model = this.model,
        netPrice = this.netPrice,
        vatId = vatId,
        stock = this.stock,
        categoryId = categoryId,
        isActive = this.isActive
    )
}