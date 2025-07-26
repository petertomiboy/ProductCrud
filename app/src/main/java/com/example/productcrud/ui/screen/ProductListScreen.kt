package com.example.productcrud.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.productcrud.viewModel.ProductViewModel
import com.example.productcrud.ui.component.ProductCard
import com.example.productcrud.dto.ProductDto
import com.example.productcrud.ui.dialog.ConfirmDeleteDialog


@Composable
fun ProductListScreen (
    viewModel: ProductViewModel,
    onAddClicked: () -> Unit,
    onEditClicked: (ProductDto) -> Unit,
    onDeleteClicked: (ProductDto) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val productList = state.products

    var productToDelete by remember { mutableStateOf<ProductDto?>(null) }
    var showDialog by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productList) { productDto ->
                    ProductCard(
                        product = productDto,
                        onEdit = { onEditClicked(productDto) },
                        onDelete = {
                            productToDelete = productDto
                            showDialog = true
                        }
                    )
                }
            }
        }
    }

    if (showDialog && productToDelete != null) {
        ConfirmDeleteDialog(
            text = "Biztosan törlöd a(z) ${productToDelete!!.name} terméket?",
            onConfirm = {
                onDeleteClicked(productToDelete!!)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}