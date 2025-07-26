package com.example.productcrud.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vat_rates")
data class VatRate(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val percentage: Int, // pld. 0, 12, 27
    val deleted: Boolean = false // Logikai törlés jelölés (soft delete)
)
