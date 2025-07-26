package com.example.productcrud.data


import com.example.productcrud.model.ProductCategory
import javax.inject.Inject

class ProductCategoryRepository @Inject constructor(
    private val dao: ProductCategoryDao
) {
    val categories = dao.getAll()

    suspend fun addCategory(name: String) {
        dao.insert(ProductCategory(name = name))
    }

    suspend fun updateCategory(category: ProductCategory) {
        dao.updateCategory(category)
    }

    suspend fun softDeleteCategory(id: Int) {
        dao.softDeleteCategory(id)
    }
}