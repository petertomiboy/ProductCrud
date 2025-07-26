package com.example.productcrud.utils.export

import android.content.Context
import android.graphics.*
import java.io.File
import java.io.FileOutputStream
import kotlin.math.cos
import kotlin.math.sin

object PieChartUtils {

    fun createCategoryPieChart(context: Context, categoryData: Map<String, Int>): File {
        val width = 600
        val height = 600
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true

        val total = categoryData.values.sum().toFloat()
        val radius = (width.coerceAtMost(height) / 2 * 0.7).toFloat()
        val centerX = width / 2f
        val centerY = height / 2f

        var startAngle = 0f

        val colors = listOf(
            Color.RED,
            Color.rgb(100, 181, 246), // világoskék
            Color.GREEN,
            Color.MAGENTA,
            Color.CYAN,
            Color.YELLOW
        )

        var colorIndex = 0

        paint.textSize = 20f
        paint.color = Color.BLACK

        categoryData.forEach { (category, count) ->
            val sweepAngle = 360f * (count / total)
            paint.color = colors[colorIndex % colors.size]

            // Körcikk rajzolása
            canvas.drawArc(
                RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius),
                startAngle,
                sweepAngle,
                true,
                paint
            )

            // Felirat pozíció
            val angle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
            val labelRadius = radius * 0.6
            val labelX = (centerX + cos(angle) * labelRadius).toFloat()
            val labelY = (centerY + sin(angle) * labelRadius).toFloat()

            paint.color = Color.BLACK
            paint.textAlign = Paint.Align.CENTER

            // Sorok közötti távolság
            val lineSpacing = 10f

            // 1. sor: kategória név
            canvas.drawText(category, labelX, labelY - lineSpacing, paint)

            // 2. sor: darabszám
            canvas.drawText("($count db)", labelX, labelY + lineSpacing, paint)

            startAngle += sweepAngle
            colorIndex++
        }

        val exportDir = File(context.filesDir, "exports")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "pie_chart.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        return file
    }
}
