package com.example.productcrud.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.productcrud.model.VatRate
import kotlinx.coroutines.flow.Flow

@Dao
interface VatRateDao {

    @Query("SELECT * FROM vat_rates WHERE deleted = 0")
    fun getAll(): Flow<List<VatRate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vatRate: VatRate):Long

    @Query("SELECT * FROM vat_rates WHERE percentage = :percentage LIMIT 1")
    suspend fun getByPercentage(percentage: Int): VatRate?

    @Update
    suspend fun updateVatRate(vatRate: VatRate)

    @Query("UPDATE vat_rates SET deleted = 1 WHERE id = :id")
    suspend fun softDeleteVatRate(id: Int)
}