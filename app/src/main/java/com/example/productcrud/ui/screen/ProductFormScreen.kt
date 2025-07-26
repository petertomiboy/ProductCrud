package com.example.productcrud.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.productcrud.model.ProductCategory
import com.example.productcrud.model.VatRate
import com.example.productcrud.viewModel.ProductViewModel
import androidx.compose.ui.Alignment
import com.example.productcrud.dto.ProductDto
import com.example.productcrud.ui.component.CancelButton
import com.example.productcrud.ui.component.SaveButton
import com.example.productcrud.ui.state.FieldState
import com.example.productcrud.validation.StringInputValidator
import com.example.productcrud.validation.PositiveNumberValidator
import com.example.productcrud.validation.SelectionValidator
import com.example.productcrud.validation.errorMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(
    viewModel: ProductViewModel,
    onBack: () -> Unit,
    existingProduct: ProductDto? = null,
    onSave: (ProductDto) -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    val vatRates by viewModel.vatRates.collectAsState()

    var name by remember { mutableStateOf(FieldState("")) }
    var brand by remember { mutableStateOf(FieldState("")) }
    var model by remember { mutableStateOf(FieldState("")) }
    var netPrice by remember { mutableStateOf(FieldState("")) }
    var stock by remember { mutableStateOf(FieldState("")) }
    var isActive by remember { mutableStateOf(true) }

    var selectedCategory by remember { mutableStateOf(FieldState<ProductCategory?>(null))}
    var selectedVat by remember { mutableStateOf(FieldState<VatRate?>(null)) }

    // Validátorok
    val nameValidator = remember { StringInputValidator("Név", minLength = 3) }
    val brandValidator = remember { StringInputValidator("Gyártó", minLength = 2) }
    val modelValidator = remember { StringInputValidator("Modell", minLength = 2) }
    val netPriceValidator = remember { PositiveNumberValidator("Nettó ár") }
    val stockValidator = remember { PositiveNumberValidator("Raktárkészlet") }
    val categoryValidator =  remember { SelectionValidator("Kategória") }
    val vatValidator = remember { SelectionValidator("ÁFA") }

    // Validációs hibák
    var nameError by remember { mutableStateOf<String?>(null) }
    var brandError by remember { mutableStateOf<String?>(null) }
    var modelError by remember { mutableStateOf<String?>(null) }
    var netPriceError by remember { mutableStateOf<String?>(null) }
    var stockError by remember { mutableStateOf<String?>(null) }
    var categoryError by remember { mutableStateOf<String?>(null) }
    var vatError by remember { mutableStateOf<String?>(null) }

    // Alapértelmezett értékek
    LaunchedEffect(categories, vatRates) {
        if (selectedCategory == null && categories.isNotEmpty()) {
            selectedCategory =
                (categories.firstOrNull { it.name.lowercase().contains("asztali") } ?: categories.first()) as FieldState<ProductCategory?>
        }
        if (selectedVat == null && vatRates.isNotEmpty()) {
            selectedVat = (vatRates.firstOrNull { it.percentage == 27 } ?: vatRates.first()) as FieldState<VatRate?>
        }
    }

    // Ha van meglévő termék, töltsük be az értékeket
    LaunchedEffect(existingProduct, categories, vatRates) {
        if (existingProduct != null && categories.isNotEmpty() && vatRates.isNotEmpty()) {
            name = FieldState(
                existingProduct.name,
                touched = true,
                error = nameValidator.validate(existingProduct.name).errorMessage
            )
            brand = FieldState(
                existingProduct.brand,
                touched = true,
                error = brandValidator.validate(existingProduct.brand).errorMessage
            )
            model = FieldState(
                existingProduct.model,
                touched = true,
                error = modelValidator.validate(existingProduct.model).errorMessage
            )
            netPrice = FieldState(
                existingProduct.netPrice.toString(),
                touched = true,
                error = netPriceValidator.validate(existingProduct.netPrice.toString()).errorMessage
            )
            stock = FieldState(
                existingProduct.stock.toString(),
                touched = true,
                error = stockValidator.validate(existingProduct.stock.toString()).errorMessage)
            isActive = existingProduct.isActive

            val categoryMatch = categories.firstOrNull { it.name == existingProduct.categoryName }
            selectedCategory = FieldState(
                value = categoryMatch,
                touched = true,
                error = categoryValidator.validate(categoryMatch).errorMessage
            )

            val vatMatch = vatRates.firstOrNull { it.percentage == existingProduct.vatValue }
            selectedVat = FieldState(
                value = vatMatch,
                touched = true,
                error = vatValidator.validate(vatMatch).errorMessage
            )

            // Debug információk - LogCat-en ellenőrizhetjük a keresett értékeket
            //println("VatValue keresés: keresett ID = ${existingProduct.vatValue}")
            //vatRates.forEach { println("VatRate: id=${it.id}, percentage=${it.percentage}") }
        }
    }

    // Realtime: form valid állapot
    val isFormValid by remember (
        name,brand,model,netPrice,stock,selectedCategory,selectedVat
    ) {
        derivedStateOf {
            name.error == null && name.value.length >= 3 &&
                    brand.error == null && brand.value.length >= 2 &&
                    model.error == null && model.value.length >= 2 &&
                    netPrice.error == null && netPrice.value.toDoubleOrNull() != null &&
                    stock.error == null && stock.value.toIntOrNull() != null &&
                    selectedCategory.error == null && selectedCategory.value != null &&
                    selectedVat.error == null && selectedVat.value != null
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = if (existingProduct == null) "Új termék hozzáadása" else "Termék szerkesztése",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = name.value,
            onValueChange = {
                val result = nameValidator.validate(it)
                name = name.copy(
                    value = it,
                    touched = true,
                    error = result.errorMessage
                )
            },
            label = { Text("Név") },
            isError = name.touched && name.error != null,
            supportingText = {

                    if (name.touched && name.error != null) {
                        Text(name.error!!, color = MaterialTheme.colorScheme.error)
                    }

            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = brand.value,
            onValueChange = {
                val result = brandValidator.validate(it)
                brand = brand.copy(
                    value = it,
                    touched = true,
                    error = result.errorMessage
                )
            },
            label = { Text("Márka") },
            isError = brand.touched && brand.error != null,
            supportingText = {

                    if (brand.touched && brand.error != null) {
                        Text(brand.error!!, color = MaterialTheme.colorScheme.error)
                    }

            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = model.value,
            onValueChange = {
                val result = modelValidator.validate(it)
                model = model.copy(
                    value = it,
                    touched = true,
                    error = result.errorMessage
                )
            },
            label = { Text("Model") },
            isError = model.touched && model.error != null,
            supportingText = {

                    if (model.touched && model.error != null) {
                        Text(model.error!!, color = MaterialTheme.colorScheme.error)
                    }

            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = netPrice.value,
            onValueChange = {
                val result = netPriceValidator.validate(it)
                netPrice = netPrice.copy(
                    value = it,
                    touched = true,
                    error = result.errorMessage
                )
            },
            label = { Text("Nettó ár") },
            isError = netPrice.touched && netPrice.error != null,
            supportingText = {
                if (netPrice.touched && netPrice.error != null) {
                    Text(netPrice.error!!, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = stock.value,
            onValueChange = {
                val result = stockValidator.validate(it)
                stock = stock.copy(
                    value = it,
                    touched = true,
                    error = result.errorMessage
                )
            },
            label = { Text("Raktármennyiség") },
            isError = stock.touched && stock.error != null,
            supportingText = {
                if (stock.touched && stock.error != null) {
                    Text(stock.error!!, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Kategória legördülő
        var expandedCategory by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expandedCategory,
            onExpandedChange = { expandedCategory = !expandedCategory }
        ) {
            OutlinedTextField(
                value = selectedCategory.value?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Kategória") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                isError = selectedCategory.touched && selectedCategory.error != null,
                supportingText = {
                    if (selectedCategory.touched && selectedCategory.error != null) {
                        Text(selectedCategory.error!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedCategory,
                onDismissRequest = { expandedCategory = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            selectedCategory = selectedCategory.copy(
                                value = category,
                                touched = true,
                                error = null
                            )
                            expandedCategory = false
                        }
                    )
                }
            }
        }

        // ÁFA legördülő
        var expandedVat by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expandedVat,
            onExpandedChange = { expandedVat = !expandedVat }
        ) {
            OutlinedTextField(
                value = selectedVat.value?.percentage?.toString()?.plus(" %") ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("ÁFA %") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedVat) },
                isError = selectedVat.touched && selectedVat.error != null,
                supportingText = {
                    if (selectedVat.touched && selectedVat.error != null) {
                        Text(selectedVat.error!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedVat,
                onDismissRequest = { expandedVat = false }
            ) {
                vatRates.forEach { vat ->
                    DropdownMenuItem(
                        text = { Text("${vat.percentage} %") },
                        onClick = {
                            selectedVat = selectedVat.copy(
                                value = vat,
                                touched = true,
                                error = null
                            )
                            expandedVat = false
                        }
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isActive = !isActive }
                .padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Checkbox(
                checked = isActive,
                onCheckedChange = { isActive = it }
            )
            Text("Aktív")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SaveButton(
                onClick = {
                    name = name.copy(error = nameValidator.validate(name.value).errorMessage, touched = true)
                    brand = brand.copy(error = brandValidator.validate(brand.value).errorMessage, touched = true)
                    model = model.copy(error = modelValidator.validate(model.value).errorMessage, touched = true)
                    netPrice = netPrice.copy(error = netPriceValidator.validate(netPrice.value).errorMessage, touched = true)
                    stock = stock.copy(error = stockValidator.validate(stock.value).errorMessage, touched = true)
                    selectedCategory = selectedCategory.copy(error = categoryValidator.validate(selectedCategory.value).errorMessage, touched = true)
                    selectedVat = selectedVat.copy(error = vatValidator.validate(selectedVat.value).errorMessage, touched = true)

                    if (isFormValid) {
                        val productDto = ProductDto(
                            id = existingProduct?.id ?: 0,
                            name = name.value,
                            brand = brand.value,
                            model = model.value,
                            netPrice = netPrice.value.toDoubleOrNull() ?: 0.0,
                            vatValue = selectedVat.value?.id ?: 0,
                            stock = stock.value.toIntOrNull() ?: 0,
                            categoryName = selectedCategory.value?.name ?: "",
                            isActive = isActive
                        )
                        onSave(productDto)
                        onBack()
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.weight(1f), //egyenlő elosztás
            )
            CancelButton(
                onClick = onBack,
                modifier = Modifier.weight(1f), //egyenlő elosztás
            )
        }
    }
}
