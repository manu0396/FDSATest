package com.example.fdsatest.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fdsatest.ui.viewmodel.SharedViewModel
import com.example.fdsatest.data.remote.models.DestinationType
import com.example.fdsatest.ui.components.AppBar
import com.example.fdsatest.ui.components.DetailRow
import com.example.fdsatest.ui.theme.FDSATestTheme
import com.example.fdsatest.ui.theme.PrimaryColor
import com.example.fdsatest.utils.DateUtils

@Composable
fun DetailDestinationScreen(
    context: Context,
    navController: NavController,
    viewModel: SharedViewModel
) {
    val destination by viewModel.selectedDestination.collectAsState()

    FDSATestTheme {
        Scaffold(
            topBar = {
                AppBar(
                    title = "Destination Details",
                    onBackClick = { navController.popBackStack() }
                )
            },
            contentWindowInsets = WindowInsets(16.dp)
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                destination?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color.White, shape = RoundedCornerShape(16.dp))
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Title and Name
                        Text(
                            text = it.name ?: "Unknown Destination",
                            style = MaterialTheme.typography.displaySmall.copy(fontSize = 26.sp),
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Detail Row for Description
                        DetailRow(
                            icon = Icons.Default.Info,
                            label = "Description",
                            content = it.description ?: "No description available"
                        )

                        // Detail Row for Country Mode
                        DetailRow(
                            icon = Icons.Default.Place,
                            label = "Country",
                            content = it.countryMode ?: "N/A"
                        )

                        // Detail Row for Type
                        DetailRow(
                            icon = Icons.Default.Place,
                            label = "Type",
                            content = it.type?.name ?: DestinationType.Country.name
                        )

                        // Detail Row for Picture URL
                        DetailRow(
                            icon = Icons.Default.Info,
                            label = "Picture URL",
                            content = it.picture ?: "No URL provided"
                        )

                        // Detail Row for Last Modify
                        DetailRow(
                            icon = Icons.Default.DateRange,
                            label = "Last Modified",
                            content = DateUtils.formatDateFromMillis(it.lastModify?.millis?: 0L)
                        )
                    }
                } ?: Text(
                    text = "Destination not found",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

