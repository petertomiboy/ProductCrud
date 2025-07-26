package com.example.productcrud.utils.export

import android.content.Context
import com.example.productcrud.dto.ProductDto
import com.google.gson.Gson
import java.io.File

object ProductJsonExport {

    fun exportToJson(context: Context, products: List<ProductDto>): String {
        val gson = Gson()
        val jsonString = gson.toJson(products)

        val exportDir = File(context.filesDir, "exports")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "products_export.json")
        file.writeText(jsonString)

        return file.absolutePath
    }
}