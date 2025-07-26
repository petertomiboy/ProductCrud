package com.example.productcrud.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.productcrud.dto.ProductDto
import com.example.productcrud.ui.component.AppDrawer
import com.example.productcrud.ui.theme.ProductCrudTheme
import com.example.productcrud.viewModel.ProductViewModel
import kotlinx.coroutines.launch
import com.example.productcrud.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: ProductViewModel) {
    var currentScreen by remember { mutableStateOf("list") }
    var selectedProductToEdit by remember { mutableStateOf<ProductDto?>(null) }
    var isDarkMode by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ProductCrudTheme(darkTheme = isDarkMode) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawer(
                    currentScreen = currentScreen,
                    onScreenSelected = { screen -> currentScreen = screen },
                    onCloseDrawer = { scope.launch { drawerState.close() } },
                    scope = scope,
                    drawerState = drawerState
                )
            }
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Image(
                                painter = painterResource(id = R.drawable.inventory_logo),
                                contentDescription = "Inventory Logo",
                                modifier = Modifier.height(100.dp)
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menü")
                            }
                        },
                        actions = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            ) {
                                Text(
                                    text = if (isDarkMode) "🌙" else "☀️",
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Switch(
                                    checked = isDarkMode,
                                    onCheckedChange = { isDarkMode = it }
                                )
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    when (currentScreen) {
                        "list" -> ProductListScreen(
                            viewModel = viewModel,
                            onAddClicked = { currentScreen = "add" },
                            onEditClicked = { product ->
                                selectedProductToEdit = product
                                currentScreen = "edit"
                            },
                            onDeleteClicked = { viewModel.deleteProduct(it) }
                        )
                        "add" -> ProductFormScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = "list" },
                            onSave = {
                                viewModel.insertProduct(it)
                                currentScreen = "list"
                            }
                        )
                        "edit" -> selectedProductToEdit?.let { product ->
                            ProductFormScreen(
                                viewModel = viewModel,
                                existingProduct = product,
                                onBack = {
                                    currentScreen = "list"
                                    selectedProductToEdit = null
                                },
                                onSave = {
                                    viewModel.updateProduct(it)
                                    currentScreen = "list"
                                    selectedProductToEdit = null
                                }
                            )
                        }
                        "statistics" -> StatisticsScreen(viewModel = viewModel)
                        "filter" -> ProductFilterScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = "list" },
                            onApplyFilters = { filters ->
                                viewModel.applyFilters(filters)
                            }
                        )
                        "vat_list" -> VatRateListScreen()
                        "category_list" -> ProductCategoryListScreen()
                        "export_json" -> ProductExportToJsonScreen(
                            onExportFinished = { currentScreen = "list" }
                        )
                        "export_pdf" -> ProductExportToPdfScreen(
                            onExportFinished = { currentScreen = "list" }
                        )
                        "import_json" -> ProductImportFromJsonScreen(
                            onImportFinished = { currentScreen = "list" }
                        )
                    }
                }
            }
        }
    }
}