package com.example.productcrud.ui.screen

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.productcrud.ui.dialog.SuccessAlertDialog
import com.example.productcrud.utils.export.ProductPdfExportWithIText
import com.example.productcrud.utils.export.ProductPdfExportWithPdfDocument
import com.example.productcrud.viewModel.ProductViewModel

@Composable
fun ProductExportToPdfScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    onExportFinished: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    var exportedFilePath by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        //val path = ProductPdfExportWithPdfDocument.exportToPdf(context, state.products) // ha mégis a PdfDocument-t használod
        val path = ProductPdfExportWithIText.exportToPdf(context, state.products)
        exportedFilePath = path
    }

    exportedFilePath?.let { path ->
        SuccessAlertDialog(
            message = "A termékek exportálva lettek PDF-be.\nElérési út: $path",
            onDismiss = {
                exportedFilePath = null
                onExportFinished()
            }
        )
    }
}
