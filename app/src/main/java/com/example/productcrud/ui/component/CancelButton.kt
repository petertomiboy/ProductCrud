package com.example.productcrud.ui.component

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CancelButton(
    onClick: () -> Unit,
    text: String = "Mégse",
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF9800),
            contentColor = Color.White
        ),
        modifier = modifier
    ) {
        Text(text)
    }
}