package com.example.productcrud.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.productcrud.model.ProductCategory
import com.example.productcrud.ui.component.CancelButton

@Composable
fun AddOrEditProductCategoryDialog(
    existingCategory: ProductCategory? = null,
    onDismiss: () -> Unit,
    onSave: (String) -> Boolean // visszaadja, hogy duplikáció volt-e
) {
    var name by remember { mutableStateOf(existingCategory?.name ?: "") }
    var isError by remember { mutableStateOf(false) }
    var isDuplicate by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (existingCategory == null) "Új kategória" else "Kategória szerkesztése")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        isError = false
                        isDuplicate = false
                    },
                    label = { Text("Név") },
                    isError = isError || isDuplicate
                )
                if (isError) {
                    Text("Add meg a nevet!", color = MaterialTheme.colorScheme.error)
                }
                if (isDuplicate) {
                    Text("Ez a kategória már létezik!", color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isBlank()) {
                    isError = true
                } else {
                    val success = onSave(name.trim())
                    if (!success) isDuplicate = true
                    else onDismiss()
                }
            }) {
                Text("Mentés")
            }
        },
        dismissButton = {
            CancelButton(
                onClick = onDismiss,
            )
        }
    )
}