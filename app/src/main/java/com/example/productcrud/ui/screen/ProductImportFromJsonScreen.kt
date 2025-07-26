package com.example.productcrud.ui.screen

import com.example.productcrud.viewModel.ProductViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.productcrud.ui.dialog.SuccessAlertDialog

@Composable
fun ProductImportFromJsonScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    onImportFinished: () -> Unit
) {
    val context = LocalContext.current
    var isSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.importProductsFromJson(context)
        isSuccess = true
    }

    if (isSuccess) {
        SuccessAlertDialog(
            message = "A JSON-ből történő importálás sikeres volt.",
            onDismiss = {
                isSuccess = false
                onImportFinished()
            }
        )
    }
}
