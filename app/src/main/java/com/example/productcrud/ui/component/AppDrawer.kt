package com.example.productcrud.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon

@Composable
fun AppDrawer (
    currentScreen: String,
    onScreenSelected: (String) -> Unit,
    onCloseDrawer: () -> Unit,
    scope: CoroutineScope,
    drawerState: DrawerState
){
    ModalDrawerSheet {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Menü",
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(
                onClick = { scope.launch { drawerState.close() } }
            ) {
                Icon(Icons.Default.Close, contentDescription = "Bezárás")
            }
        }

        HorizontalDivider()

        NavigationDrawerItem(
            label = { Text("Termékek") },
            icon = {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Termékek")
            },
            selected = currentScreen == "list",
            onClick = {
                scope.launch { drawerState.close() }
                onScreenSelected("list")
            }
        )

        NavigationDrawerItem(
            label = { Text("Új termék") },
            icon = {
                Icon(Icons.Default.Save, contentDescription = "Új termék")
            },
            selected = currentScreen == "add",
            onClick = {
                scope.launch { drawerState.close() }
                onScreenSelected("add")
            }
        )

        NavigationDrawerItem(
            label = { Text("Statisztikák") },
            icon = {
                Icon(Icons.Default.BarChart, contentDescription = "Statisztikák")
            },
            selected = currentScreen == "statistics",
            onClick = {
                scope.launch { drawerState.close() }
                onScreenSelected("statistics")
            }
        )

        NavigationDrawerItem(
            label = { Text("Termék szűrő") },
            icon = {
                Icon(Icons.Default.FilterList, contentDescription = "Szűrő")
            },
            selected = currentScreen == "filter",
            onClick = {
                scope.launch { drawerState.close() }
                onScreenSelected("filter")
            }
        )
        NavigationDrawerItem(
            label = { Text("ÁFA kulcsok") },
            icon = {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "ÁFA kulcsok")
            },
            selected = currentScreen == "vat_list",
            onClick = {
                scope.launch { drawerState.close() }
                onScreenSelected("vat_list")
            }
        )
        NavigationDrawerItem(
            label = { Text("Termékkategóriák") },
            icon = {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Kategóriák")
            },
            selected = currentScreen == "category_list",
            onClick = {
                scope.launch { drawerState.close() }
                onScreenSelected("category_list")
            }
        )
        NavigationDrawerItem(
            label = { Text("Termékek exportálása JSON-be") },
            icon = {
                Icon(Icons.Default.FileDownload, contentDescription = "Export JSON")
            },
            selected = currentScreen == "export_json",
            onClick = {
                scope.launch { drawerState.close() }
                onScreenSelected("export_json")
            }
        )
        NavigationDrawerItem(
            label = { Text("Termékek exportálása PDF-be") },
            icon = {
                Icon(Icons.Default.PictureAsPdf, contentDescription = "Export PDF")
            },
            selected = currentScreen == "export_pdf",
            onClick = {
                scope.launch { drawerState.close() }
                onScreenSelected("export_pdf")
            }
        )
        NavigationDrawerItem(
            label = { Text("Termékek importálása JSON-ből") },
            icon = {
                Icon(Icons.Default.FileUpload, contentDescription = "Import JSON")
            },
            selected = currentScreen == "import_json",
            onClick = {
                scope.launch { drawerState.close() }
                onScreenSelected("import_json")
            }
        )


    }
}