package com.example.productcrud.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.productcrud.ui.component.CancelButton

@Composable
fun AddVatRateDialog(
    onDismiss: () -> Unit,
    onAdd: (Int) -> Boolean // Visszatérési érték: true, ha hozzáadta, false, ha már létezett
) {
    var percentageText by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isDuplicate by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Új ÁFA kulcs felvitel") },
        text = {
            Column {
                OutlinedTextField(
                    value = percentageText,
                    onValueChange = {
                        percentageText = it
                        isError = false // Reset error state on input change},
                        isDuplicate = false // Reset duplicate state on input change
                    },
                    label = { Text("ÁFA kulcs (%)") },
                    isError = isError || isDuplicate,
                    singleLine = true
                )
                if (isError) {
                    Text(
                        "Kérjük, adjon meg egy érvényes számot.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                if (isDuplicate) {
                    Text(
                        "Ez az ÁFA kulcs már létezik.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val percentage = percentageText.toIntOrNull()
                    if (percentage != null && percentage >= 0) {
                        val success = onAdd(percentage)
                        if (!success) {
                            isDuplicate = true
                        }
                    } else {
                        isError = true
                    }
                }
            ) {
                Text("Hozzáadás")
            }
        },
        dismissButton = {
            CancelButton(
                onClick = onDismiss,
            )
        }
    )
}