package com.example.productcrud.ui.state

// Többi képernyő is fel tudja használni, ezért külön fájlba került: pld. CategoryFormState.kt
data class FieldState<T>(
    val value: T,
    val error: String? = null,
    val touched: Boolean = false
)