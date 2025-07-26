package com.example.productcrud.utils.import

import android.content.Context
import android.util.Log
import com.example.productcrud.dto.ProductDto
import com.google.gson.Gson
import java.io.File

object ProductJsonImport {

    fun importFromJson(context: Context): List<ProductDto> {
        val file = File(context.filesDir, "exports/products_export.json")

        if (!file.exists()) {
            throw IllegalStateException("A JSON export fájl nem található!")
        }

        val jsonString = file.readText()
        val gson = Gson()
        Log.d("ProductJsonImport", "JSON importálás: $jsonString")
        return gson.fromJson(jsonString, Array<ProductDto>::class.java).toList()
    }
}