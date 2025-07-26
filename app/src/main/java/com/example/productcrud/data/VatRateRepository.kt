package com.example.productcrud.data

import com.example.productcrud.model.VatRate
import javax.inject.Inject

class VatRateRepository @Inject constructor(
    private val vatRateDao: VatRateDao
) {
    val vatRates = vatRateDao.getAll()

    suspend fun addVatRate(percentage: Int) {
        vatRateDao.insert(VatRate(percentage = percentage))
    }

    suspend fun softDeleteVatRate(id: Int) {
        vatRateDao.softDeleteVatRate(id)
    }
}