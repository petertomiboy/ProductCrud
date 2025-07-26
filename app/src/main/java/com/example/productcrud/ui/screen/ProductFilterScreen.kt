package com.example.productcrud.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.productcrud.model.ProductCategory
import com.example.productcrud.viewModel.ProductViewModel
import com.example.productcrud.model.FilterCriteria
import com.example.productcrud.ui.component.CancelButton

// Compose dropdown
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults

import androidx.compose.material3.DropdownMenuItem
import com.example.productcrud.ui.component.DeleteButton
import com.example.productcrud.ui.component.FilterToggleButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFilterScreen(
    viewModel: ProductViewModel,
    onBack: () -> Unit,
    onApplyFilters: (FilterCriteria) -> Unit
) {
    val currentFilter by viewModel.filterCriteria.collectAsState()
    val categories by viewModel.categories.collectAsState()
    var selectedCategory by remember { mutableStateOf(currentFilter?.category) }
    var isActive by remember { mutableStateOf(currentFilter?.isActive) }
    var minPrice by remember { mutableStateOf(currentFilter?.minPrice?.toString() ?: "") }
    var maxPrice by remember { mutableStateOf(currentFilter?.maxPrice?.toString() ?: "") }
    var minStock by remember { mutableStateOf(currentFilter?.minStock?.toString() ?: "") }
    var maxStock by remember { mutableStateOf(currentFilter?.maxStock?.toString() ?: "") }
    var searchQuery by remember { mutableStateOf(currentFilter?.query ?: "") }

    Column(
        Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Termék szűrő", style = MaterialTheme.typography.titleLarge)

        // Kategória legördülő
        var expandedCategory by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expandedCategory,
            onExpandedChange = { expandedCategory = !expandedCategory }
        ) {
            OutlinedTextField(
                value = selectedCategory?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Kategória") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedCategory,
                onDismissRequest = { expandedCategory = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Összes") },
                    onClick = {
                        selectedCategory = null
                        expandedCategory = false
                    }
                )
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            selectedCategory = category
                            expandedCategory = false
                        }
                    )
                }
            }
        }

        // Aktív / Inaktív választó
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterToggleButton(
                text = "Összes",
                selected = isActive == null,
                onClick = { isActive = null }
            )
            FilterToggleButton(
                text = "Aktív",
                selected = isActive == true,
                onClick = { isActive = true }
            )
            FilterToggleButton(
                text = "Inaktív",
                selected = isActive == false,
                onClick = { isActive = false }
            )
        }

        // Ár intervallum
        OutlinedTextField(
            value = minPrice,
            onValueChange = { minPrice = it },
            label = { Text("Min. ár") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = maxPrice,
            onValueChange = { maxPrice = it },
            label = { Text("Max. ár") },
            modifier = Modifier.fillMaxWidth()
        )

        // Raktármennyiség intervallum
        OutlinedTextField(
            value = minStock,
            onValueChange = { minStock = it },
            label = { Text("Min. készlet") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = maxStock,
            onValueChange = { maxStock = it },
            label = { Text("Max. készlet") },
            modifier = Modifier.fillMaxWidth()
        )

        // Név / Márka / Modell szűrés
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Név, márka, modell") },
            modifier = Modifier.fillMaxWidth()
        )

        // Gombok
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    onApplyFilters(
                        FilterCriteria(
                            category = selectedCategory,
                            isActive = isActive,
                            minPrice = minPrice.toDoubleOrNull(),
                            maxPrice = maxPrice.toDoubleOrNull(),
                            minStock = minStock.toIntOrNull(),
                            maxStock = maxStock.toIntOrNull(),
                            query = searchQuery
                        )
                    )
                    onBack()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Szűrés")
            }
            DeleteButton(
                onClick = {
                    // Összes szűrési mező visszaállítása
                    selectedCategory = null
                    isActive = null
                    minPrice = ""
                    maxPrice = ""
                    minStock = ""
                    maxStock = ""
                    searchQuery = ""

                    // ViewModel filterCriteria törlése
                    onApplyFilters(FilterCriteria())  // üres feltételekkel

                },
                modifier = Modifier.weight(1f)
            )
            CancelButton(onClick = onBack, modifier = Modifier.weight(1f))
        }
    }
}