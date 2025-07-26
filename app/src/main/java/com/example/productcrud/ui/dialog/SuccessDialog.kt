package com.example.productcrud.ui.dialog

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun SuccessAlertDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sikeres exportálás") },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}
