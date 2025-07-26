package com.example.productcrud.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.productcrud.dto.ProductDto
import com.example.productcrud.model.Product
import com.example.productcrud.ui.theme.DeleteRed
import com.example.productcrud.ui.theme.EditGreen

@Composable
fun ProductCard(
    product: ProductDto,
    onEdit: () -> Unit,
    onDelete: () -> Unit

) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${product.id} ${product.name} (${product.brand} ${product.model})", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Nettó ár: ${product.netPrice} Ft")
            Text("ÁFA: ${product.vatValue}%", style = MaterialTheme.typography.bodySmall)
            Text("Raktárkészlet: ${product.stock} db")
            Text("Kategória: ${product.categoryName}")
            Text("Állapot: ${if (product.isActive) "Aktív" else "Inaktív"}")

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Szerkesztés",
                        tint = EditGreen // zöld a Color.kt fájlban definiált EditGreen szín használata
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Törlés",
                        tint = DeleteRed // Piros szín a törlés ikonhoz a Color.kt fájlban definiált DeleteRed szín használata
                    )
                }
            }
        }
    }
}