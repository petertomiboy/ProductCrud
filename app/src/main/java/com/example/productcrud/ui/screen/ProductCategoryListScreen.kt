package com.example.productcrud.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.productcrud.model.ProductCategory
import com.example.productcrud.ui.dialog.AddOrEditProductCategoryDialog
import com.example.productcrud.ui.dialog.ConfirmDeleteDialog
import com.example.productcrud.ui.theme.DeleteRed
import com.example.productcrud.ui.theme.EditGreen
import com.example.productcrud.viewModel.ProductCategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCategoryListScreen(
    viewModel: ProductCategoryViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<ProductCategory?>(null) }
    var categoryToDelete by remember { mutableStateOf<ProductCategory?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Termékkategóriák") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Új kategória")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize().padding(8.dp)
        ) {
            items(categories) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(category.name, style = MaterialTheme.typography.bodyLarge)
                        Row {
                            IconButton(onClick = { categoryToEdit = category }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Szerkesztés",
                                    tint = EditGreen
                                )
                            }
                            IconButton(onClick = { categoryToDelete = category }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Törlés",
                                    tint = DeleteRed
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Új kategória felvitel dialógus
    if (showAddDialog) {
        AddOrEditProductCategoryDialog(
            onDismiss = { showAddDialog = false },
            onSave = { name ->
                val added = viewModel.addCategoryIfNotExists(name)
                added // false = duplikáció
            }
        )
    }

    // Kategória szerkesztés dialógus
    categoryToEdit?.let { category ->
        AddOrEditProductCategoryDialog(
            existingCategory = category,
            onDismiss = { categoryToEdit = null },
            onSave = { name ->
                val updated = if (category.name != name) {
                    val exists = viewModel.categories.value.any {
                        it.name.equals(name, ignoreCase = true)
                    }
                    if (!exists) {
                        viewModel.updateCategory(category.copy(name = name))
                    }
                    !exists
                } else true
                updated
            }
        )
    }

    // Törlés megerősítő dialógus
    categoryToDelete?.let { category ->
        ConfirmDeleteDialog(
            text = "Biztosan törlöd a(z) ${category.name} kategóriát?",
            onConfirm = {
                viewModel.softDeleteCategory(category.id)
                categoryToDelete = null
            },
            onDismiss = { categoryToDelete = null }
        )
    }
}