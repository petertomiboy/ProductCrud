package com.example.productcrud.utils.export

import android.content.Context
import com.example.productcrud.dto.ProductDto
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.layout.properties.HorizontalAlignment
import java.io.File

object ProductPdfExportWithIText {

    fun exportToPdf(context: Context, products: List<ProductDto>): String {
        val exportDir = File(context.filesDir, "exports")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }
        val file = File(exportDir, "products_export.pdf")
        val pdfWriter = PdfWriter(file)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        val font = PdfFontFactory.createFont()

        val grouped = products.groupBy { it.categoryName }
        val categories = grouped.keys.sorted()

        val headers = listOf("ID", "Név", "Kategória", "Nettó ár", "Raktár", "Aktív")

        categories.forEach { category ->
            val productList = grouped[category]!!
            val totalStock = productList.sumOf { it.stock }
            val totalNet = productList.sumOf { it.netPrice }

            // Kategória fejléc
            document.add(
                Paragraph("Kategória: $category")
                    .setBold()
                    .setFontSize(14f)
                    .setMarginBottom(5f)
            )

            // Táblázat
            val table = Table(floatArrayOf(40f, 150f, 100f, 70f, 70f, 50f))
                .useAllAvailableWidth()

            headers.forEach {
                table.addHeaderCell(
                    Cell().add(Paragraph(it).setBold())
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setBorder(SolidBorder(1f))
                )
            }

            productList.forEach { product ->
                table.addCell(
                    Cell().add(Paragraph("${product.id}"))
                        .setBorder(SolidBorder(0.5f))
                )
                table.addCell(
                    Cell().add(Paragraph(product.name))
                        .setBorder(SolidBorder(0.5f))
                )
                table.addCell(
                    Cell().add(Paragraph(product.categoryName))
                        .setBorder(SolidBorder(0.5f))
                )
                table.addCell(
                    Cell().add(Paragraph("${product.netPrice}"))
                        .setBorder(SolidBorder(0.5f))
                )
                table.addCell(
                    Cell().add(Paragraph("${product.stock}"))
                        .setBorder(SolidBorder(0.5f))
                )
                table.addCell(
                    Cell().add(Paragraph(if (product.isActive) "Igen" else "Nem"))
                        .setBorder(SolidBorder(0.5f))
                )
            }

            document.add(table)

            document.add(Paragraph("Összes raktármennyiség: $totalStock db").setMarginTop(5f))
            document.add(Paragraph("Összes nettó ár: $totalNet Ft").setMarginBottom(10f))
        }

        // Összes kategória és darabszám lista
//        document.add(Paragraph("Összes kategória:").setBold().setFontSize(14f))
//        categories.forEach {
//            val stockSum = grouped[it]?.sumOf { p -> p.stock } ?: 0
//            document.add(Paragraph("- $it ($stockSum db)"))
//        }

        // Tortadiagram: IText nem támogatja a diagramok közvetlen rajzolását, de képként beilleszthetjük
        val categoryCounts = grouped.mapValues { it.value.sumOf { p -> p.stock } }
        val chartFile = PieChartUtils.createCategoryPieChart(context, categoryCounts)
        val imageData = com.itextpdf.io.image.ImageDataFactory.create(chartFile.absolutePath)
        val image = com.itextpdf.layout.element.Image(imageData)
            .scaleToFit(250f, 250f) // kisebb
            .setHorizontalAlignment(HorizontalAlignment.CENTER) // középre helyezve

        document.add(Paragraph("\nKategóriák raktár szerinti aránya:").setBold().setFontSize(14f))
        document.add(image)

        document.close()
        return file.absolutePath
    }
}
