package com.example.productcrud.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.productcrud.model.VatRate
import com.example.productcrud.ui.dialog.AddVatRateDialog
import com.example.productcrud.ui.dialog.ConfirmDeleteDialog
import com.example.productcrud.viewModel.VatRateViewModel
import com.example.productcrud.ui.theme.DeleteRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VatRateListScreen(
    viewModel: VatRateViewModel = hiltViewModel()
) {
    val vatRates by viewModel.vatRates.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var vatRateToDelete by remember { mutableStateOf<VatRate?>(null) }

    Scaffold (
        topBar = {
            TopAppBar(title = {Text("ÁFA kulcsok")})
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true}) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(vatRates) { vatRate ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${vatRate.percentage}%", style = MaterialTheme.typography.bodyLarge)
                        IconButton(onClick = {vatRateToDelete = vatRate}) {
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
    if (showAddDialog) {
        AddVatRateDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { percentage ->
                val added = viewModel.addVatRateIfNotExists(percentage)
                if (added) {
                    showAddDialog = false
                }
                added // Ha nem added, a dialog mutatja a hibát (isDuplicate flag)
            }
        )
    }

    vatRateToDelete?.let { vatRate ->
        ConfirmDeleteDialog(
            text = "Biztosan törlöd a(z) ${vatRate.percentage}% ÁFA kulcsot?",
            onConfirm = {
                viewModel.softDeleteVatRate(vatRate.id)
                vatRateToDelete = null
            },
            onDismiss = { vatRateToDelete = null }
        )
    }
}