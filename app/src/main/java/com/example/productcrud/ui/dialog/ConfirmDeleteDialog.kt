package com.example.productcrud.ui.dialog

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.productcrud.dto.ProductDto
import com.example.productcrud.ui.component.CancelButton
import com.example.productcrud.ui.component.DeleteButton

@Composable
fun ConfirmDeleteDialog(
    title: String = "Törlés megerősítés",
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Text(
                text = text
            )
        },
        confirmButton = {
            DeleteButton(onClick = onConfirm)
        },
        dismissButton = {
            CancelButton(onClick = onDismiss)
        }
    )
}