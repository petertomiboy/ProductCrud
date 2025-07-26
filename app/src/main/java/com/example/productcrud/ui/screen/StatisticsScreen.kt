package com.example.productcrud.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.productcrud.viewModel.ProductViewModel

@Composable
fun StatisticsScreen(viewModel: ProductViewModel) {
    val state by viewModel.state.collectAsState()
    val products = state.products

    val categoryCounts = products.groupBy { it.categoryName }.mapValues { it.value.size }
    val activeInactiveByCategory = products.groupBy { it.categoryName }
        .mapValues { entry ->
            val active = entry.value.count { it.isActive }
            val inactive = entry.value.size - active
            "Aktív: $active, Inaktív: $inactive"
        }
    val mostExpensive = products.maxByOrNull { it.netPrice }
    val cheapest = products.minByOrNull { it.netPrice }
    val mostInStock = products.maxByOrNull { it.stock }
    val leastInStock = products.minByOrNull { it.stock }
    val brandStock = products.groupBy { it.brand }
        .mapValues { entry -> entry.value.sumOf { it.stock } }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("📊 Statisztikák", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(12.dp))
        Text("Kategóriánkénti termékszám:")
        categoryCounts.forEach { (category, count) ->
            Text("- $category: $count db")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Aktív/Inaktív termékek kategóriánként:")
        activeInactiveByCategory.forEach { (category, status) ->
            Text("- $category: $status")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Legdrágább termék: ${mostExpensive?.name} (${mostExpensive?.netPrice} Ft)")
        Text("Legolcsóbb termék: ${cheapest?.name} (${cheapest?.netPrice} Ft)")

        Spacer(modifier = Modifier.height(12.dp))
        Text("Legtöbb raktáron: ${mostInStock?.name} (${mostInStock?.stock} db)")
        Text("Legkevesebb raktáron: ${leastInStock?.name} (${leastInStock?.stock} db)")

        Spacer(modifier = Modifier.height(12.dp))
        Text("Gyártónkénti raktárkészlet:")
        brandStock.forEach { (brand, stock) ->
            Text("- $brand: $stock db")
        }
    }
}