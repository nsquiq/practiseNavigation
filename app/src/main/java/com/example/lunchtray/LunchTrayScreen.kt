/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.ui.EntreeMenuScreen
import com.example.lunchtray.ui.OrderViewModel
import com.example.lunchtray.ui.StartOrderScreen

import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.ui.AccompanimentMenuScreen
import com.example.lunchtray.ui.CheckoutScreen
import com.example.lunchtray.ui.SideDishMenuScreen

// TODO: Screen enum

// TODO: AppBar

@OptIn(ExperimentalMaterial3Api::class)


enum class LunchTrayScreen(){
    Start,
    EntreeMenu,
    SideDishMenu,
    AccompanimentMenu,
    Checkout
}
@Composable
fun LunchTrayApp() {
    // TODO: Create Controller and initialization
    val navController = rememberNavController()
    // Create ViewModel
    val viewModel: OrderViewModel = viewModel()

    Scaffold(
        topBar = {
            // TODO: AppBar
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = LunchTrayScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = LunchTrayScreen.Start.name){
                StartOrderScreen(
                    onStartOrderButtonClicked = {
                        navController.navigate(LunchTrayScreen.EntreeMenu.name)

                    },
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(innerPadding))
            }
            composable(route = LunchTrayScreen.EntreeMenu.name) {
            EntreeMenuScreen(
                options = DataSource.entreeMenuItems,
                onCancelButtonClicked = {
                    viewModel.resetOrder()
                    navController.popBackStack(LunchTrayScreen.Start.name, inclusive = false)

                },
                onNextButtonClicked = { navController.navigate(LunchTrayScreen.SideDishMenu.name) },
                onSelectionChanged = { item ->
                    viewModel.updateEntree(item)
                },
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
            )

                
            }
            composable(route = LunchTrayScreen.SideDishMenu.name) {
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onCancelButtonClicked = { viewModel.resetOrder()
                        navController.popBackStack(LunchTrayScreen.Start.name, inclusive = false) },
                    onNextButtonClicked = {  navController.navigate(LunchTrayScreen.AccompanimentMenu.name) },
                    onSelectionChanged = { item ->
                        viewModel.updateSideDish(item)
                    },
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(innerPadding)
                )
        }
            composable(route = LunchTrayScreen.AccompanimentMenu.name) {
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = {
                             viewModel.resetOrder()
                        navController.popBackStack(LunchTrayScreen.Start.name,inclusive = false)
                    },
                    onNextButtonClicked = { 
                       navController.navigate(LunchTrayScreen.Checkout.name)
                    },
                    onSelectionChanged = {
                        item ->
                        viewModel.updateAccompaniment(item)
                    },
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(innerPadding)
                )
            }
            composable(route = LunchTrayScreen.Checkout.name) {
                CheckoutScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(LunchTrayScreen.Start.name, inclusive = false)
                    },
                    onNextButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(LunchTrayScreen.Start.name, inclusive = false)
                    },
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                )
            }

        }

    }
}
