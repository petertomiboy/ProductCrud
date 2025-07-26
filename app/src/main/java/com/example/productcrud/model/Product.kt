package com.example.productcrud.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = VatRate::class,
            parentColumns = ["id"],
            childColumns = ["vatId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = ProductCategory::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
)

data class Product (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val brand: String,
    val model:String,
    val netPrice: Double,
    val vatId: Int?,
    val stock: Int,
    val categoryId: Int?,
    val isActive: Boolean,
)