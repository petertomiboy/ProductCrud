package com.example.productcrud.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.productcrud.model.Product
import com.example.productcrud.model.ProductCategory
import com.example.productcrud.model.VatRate

@Database(
    entities = [Product::class, ProductCategory::class, VatRate::class],
    version = 3,
    exportSchema = false
)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun productCategoryDao(): ProductCategoryDao
    abstract fun vatRateDao(): VatRateDao
}