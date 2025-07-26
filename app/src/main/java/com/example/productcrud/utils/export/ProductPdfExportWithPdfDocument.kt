package com.example.productcrud.utils.export

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.productcrud.dto.ProductDto
import java.io.File

object ProductPdfExportWithPdfDocument {

    fun exportToPdf(context: Context, products: List<ProductDto>): String {
        val pdfDocument = PdfDocument()
        val paint = Paint()

        var pageNumber = 1

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create() // A4 méret
        var page = pdfDocument.startPage(pageInfo)
        var canvas: Canvas = page.canvas

        var y = 50

        // Oldal fejléc
        paint.textSize = 20f
        paint.isFakeBoldText = true
        canvas.drawText("Termék lista PDF export", 40f, y.toFloat(), paint)

        y += 40
        paint.textSize = 14f
        paint.isFakeBoldText = true
        canvas.drawText("ID", 40f, y.toFloat(), paint)
        canvas.drawText("Név", 80f, y.toFloat(), paint)
        canvas.drawText("Ár (nettó)", 300f, y.toFloat(), paint)
        canvas.drawText("Raktár", 400f, y.toFloat(), paint)

        paint.isFakeBoldText = false
        y += 20

        products.forEach { product ->
            y += 20

            if (y > 800) {
                pdfDocument.finishPage(page)
                pageNumber++

                val newPageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
                page = pdfDocument.startPage(newPageInfo)
                canvas = page.canvas

                y = 50

                // Új oldal fejléc újra
                paint.textSize = 20f
                paint.isFakeBoldText = true
                canvas.drawText("Termék lista PDF export (folytatás)", 40f, y.toFloat(), paint)

                y += 40
                paint.textSize = 14f
                paint.isFakeBoldText = true
                canvas.drawText("ID", 40f, y.toFloat(), paint)
                canvas.drawText("Név", 80f, y.toFloat(), paint)
                canvas.drawText("Ár (nettó)", 300f, y.toFloat(), paint)
                canvas.drawText("Raktár", 400f, y.toFloat(), paint)

                paint.isFakeBoldText = false
                y += 20
            }

            canvas.drawText("${product.id}", 40f, y.toFloat(), paint)
            canvas.drawText(product.name, 80f, y.toFloat(), paint)
            canvas.drawText("${product.netPrice}", 300f, y.toFloat(), paint)
            canvas.drawText("${product.stock}", 400f, y.toFloat(), paint)
        }

        pdfDocument.finishPage(page)

        val exportDir = File(context.filesDir, "exports")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "products_export.pdf")
        pdfDocument.writeTo(file.outputStream())
        pdfDocument.close()

        return file.absolutePath
    }
}

