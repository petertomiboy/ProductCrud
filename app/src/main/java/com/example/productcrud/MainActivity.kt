package com.example.productcrud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.productcrud.ui.screen.MainScreen
import com.example.productcrud.viewModel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: ProductViewModel by viewModels()

        setContent {
            MainScreen(viewModel = viewModel)
        }
    }
}