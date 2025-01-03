package com.example.fdsatest.ui.screens

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fdsatest.R
import com.example.fdsatest.data.remote.models.DestinationType
import com.example.fdsatest.data.remote.models.Timestamp
import com.example.fdsatest.domain.models.DestinationDomain
import com.example.fdsatest.ui.animations.TripleOrbitLoadingAnimation
import com.example.fdsatest.ui.components.AppBar
import com.example.fdsatest.ui.components.BottomNavigationBar
import com.example.fdsatest.ui.components.DropdownSelector
import com.example.fdsatest.ui.components.EditableTable
import com.example.fdsatest.ui.components.H1Text
import com.example.fdsatest.ui.components.H2Text
import com.example.fdsatest.ui.components.SimpleAlertDialog
import com.example.fdsatest.ui.components.VerticalDataSelector
import com.example.fdsatest.ui.theme.FDSATestTheme
import com.example.fdsatest.ui.theme.PrimaryColor
import com.example.fdsatest.ui.viewmodel.SharedViewModel
import com.example.fdsatest.utils.DateUtils
import com.example.fdsatest.utils.NetworkUtils

@Composable
fun DestinationScreen(
    context: Context,
    viewModel: SharedViewModel,
    navController: NavController,
) {
    // Collect state from ViewModel
    val data by viewModel.data.collectAsState()
    val localData by viewModel.localData.collectAsState()
    val showLoading by viewModel.showLoading.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val messageDialog by viewModel.messageDialog.collectAsState()

    val filterData = if (NetworkUtils.checkConnectivity(context)) {
        data
    } else {
        localData
    }

// State for managing search query and results
    var searchQuery by remember { mutableStateOf("") }
    var searchResultIndex by remember { mutableStateOf<List<Int>>(emptyList()) }

    val filteredData = if (searchResultIndex.isNotEmpty()) {
        searchResultIndex.mapNotNull { index -> data.getOrNull(index) }
    } else {
        data
    }

    // State for managing selected row index
    val selectedRowIndex by viewModel.selectedRowIndex.collectAsState()

    // State for managing create and modify dialogs
    val showDialogCreate by viewModel.showDialogCreate.collectAsState()
    val showDialogModify by viewModel.showDialogModify.collectAsState()
    val showDialogDelete by viewModel.showDialogDelete.collectAsState()
    var createDestinationId by remember { mutableStateOf("") }
    var createDestinationName by remember { mutableStateOf("") }
    var createDestinationDescription by remember { mutableStateOf("") }
    var createDestinationCountryMode by remember { mutableStateOf("") }
    var createDestinationType by remember { mutableStateOf(DestinationType.Country.name) }
    var createDestinationPicture by remember { mutableStateOf("") }
    var createDestinationLastModify by remember {
        mutableStateOf(
            Timestamp(
                System.currentTimeMillis()
            )
        )
    }
// State for managing modify mode
    var isModifyMode by remember { mutableStateOf(false) }

    val isPortrait = when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            false
        }

        Configuration.ORIENTATION_PORTRAIT -> {
            true
        }

        else -> {
            false
        }
    }


    // Launched effect to trigger initial data fetching
    LaunchedEffect(key1 = true) {
        viewModel.getResults(context)
        NetworkUtils.checkConnectivity(context)
    }

    // Scaffold and UI components
    FDSATestTheme {
        // Show dialog for errors if showDialog is true
        if (showDialog) {
            SimpleAlertDialog(
                context = context,
                show = true,
                title = context.getString(R.string.titleError),
                text = messageDialog,
                onConfirm = viewModel::onDialogConfirm,
                onDismiss = viewModel::onDialogDismiss,
                elevation = 10
            )
        }

        // Main Scaffold with app bar and content
        Scaffold(
            topBar = {
                AppBar(
                    title = context.getString(R.string.main_title),
                    onBackClick = { navController.popBackStack() }
                )
            },
            bottomBar = { BottomNavigationBar(navController) }
        ) { paddingValues ->
            // Box to contain main content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(color = Color.White)
            ) {
                // Title
                H1Text(
                    text = context.getString(R.string.destination_title),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp, bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                // Search and Filter Column
                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    // VerticalDataSelector with Search Button
                    VerticalDataSelector(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 0.dp, vertical = 8.dp),
                        data = filterData.map { it?.name ?: it?.id.toString() },
                        viewModel = viewModel,
                        context = context,
                        onSearchPerformed = { index ->
                            searchResultIndex = index // Set the index for filtering
                        }
                    )

                    // Display EditableTable with data
                    EditableTable(
                        data = filteredData,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = if (!isPortrait) 0.dp else 16.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                color = Color(0xFFaac6f5),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp),
                        onCellSelected = { rowIndex ->
                            viewModel.setSelectedDestinationIndex(rowIndex)
                            viewModel.data.value.getOrNull(rowIndex)?.let { destination ->
                                viewModel.setSelectedDestination(destination)
                            }
                        },
                        selectedRowIndex = remember { mutableStateOf(selectedRowIndex) },
                        onRefresh = {
                            viewModel.getResults(context, isRefreshing = true)
                        },
                        navController = navController,
                        viewModel = viewModel,
                        isModifyMode = isModifyMode
                    )

                }
                // Display loading indicator or "No data" message
                if (showLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        TripleOrbitLoadingAnimation()
                    }
                } else if (filteredData.isEmpty() && searchQuery.isNotEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        H2Text(text = "No matching results found", modifier = Modifier)
                    }
                }
                // Loading or Error message when data is loading or empty
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 120.dp, bottom = 120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (showLoading) {
                        TripleOrbitLoadingAnimation()
                    } else {
                        if (filterData.isEmpty()) {
                            H2Text(
                                text = context.getString(R.string.no_data_found),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }

                // Row of action buttons (Create, Modify, Delete)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = if (!isPortrait) 0.dp else 8.dp)
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Create button
                    FloatingActionButton(
                        shape = RoundedCornerShape(50),
                        onClick = {
                            viewModel.showDialogCreate(true)
                        },
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f),
                        containerColor = PrimaryColor,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Create")
                    }

                    // Modify button
                    FloatingActionButton(
                        shape = RoundedCornerShape(50),
                        onClick = {
                            selectedRowIndex?.let { rowIndex ->
                                viewModel.showDialogModify(true)
                                val selectedDestination = filterData[rowIndex]
                                createDestinationName = selectedDestination?.name ?: ""
                                createDestinationDescription =
                                    selectedDestination?.description ?: ""
                                createDestinationCountryMode =
                                    selectedDestination?.countryMode ?: ""
                                createDestinationType =
                                    selectedDestination?.type?.name ?: DestinationType.Country.name
                                createDestinationPicture = selectedDestination?.picture ?: ""
                                createDestinationLastModify =
                                    selectedDestination?.lastModify ?: Timestamp(0)
                            } ?: viewModel.showDialog(context.getString(R.string.error_modify))
                        },
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f),
                        containerColor = Color(0xFF34A853),
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Modify")
                    }

                    // Delete button
                    FloatingActionButton(
                        shape = RoundedCornerShape(50),
                        onClick = {
                            selectedRowIndex?.let { _ ->
                                viewModel.showDialogDelete(true)
                            } ?: viewModel.showDialog(context.getString(R.string.error_delete))
                        },
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f),
                        containerColor = Color(0xFFEA4335),
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Create")
                    }

                    // Create Destination Dialog
                    if (showDialogCreate) {
                        AlertDialog(
                            onDismissRequest = { viewModel.showDialogCreate(false) },
                            title = { Text(text = "Create New Destination") },
                            text = {
                                Column {
                                    Text("ID:")
                                    Spacer(modifier = Modifier.size(8.dp))
                                    TextField(
                                        value = createDestinationId,
                                        onValueChange = { createDestinationId = it },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text("Name:")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    TextField(
                                        value = createDestinationName,
                                        onValueChange = { createDestinationName = it },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text("Description:")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    TextField(
                                        value = createDestinationDescription,
                                        onValueChange = { createDestinationDescription = it },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text("Country Mode:")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    TextField(
                                        value = createDestinationCountryMode,
                                        onValueChange = { createDestinationCountryMode = it },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text("Type:")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    DropdownSelector(
                                        label = context.getString(R.string.typeLabel),
                                        options = listOf("City", "Country"),
                                        selectedOption = createDestinationType,
                                        onOptionSelected = { selectedType -> createDestinationType = selectedType }
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text("Picture:")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    TextField(
                                        value = createDestinationPicture,
                                        onValueChange = { createDestinationPicture = it },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text("Last Modify:")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    BasicTextField(
                                        value = createDestinationLastModify.toString(),
                                        onValueChange = {
                                            createDestinationLastModify = Timestamp(it.toLong())
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        if (createDestinationId.isNotBlank() && createDestinationName.isNotBlank()) {
                                            viewModel.createDestination(
                                                DestinationDomain(
                                                    id = createDestinationId,
                                                    name = createDestinationName,
                                                    description = createDestinationDescription,
                                                    countryMode = createDestinationCountryMode,
                                                    type = DestinationType.valueOf(
                                                        createDestinationType
                                                    ),
                                                    picture = createDestinationPicture,
                                                    lastModify = createDestinationLastModify
                                                )
                                            )
                                            viewModel.showDialogCreate(false) // Close dialog
                                        } else {
                                            viewModel.showError("ID and Name cannot be empty") // Show error if essential fields are missing
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PrimaryColor
                                    )
                                ) {
                                    Text("Create")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { viewModel.showDialogCreate(false) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Red
                                    ),
                                ) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                }

                // Modify Destination Dialog
                if (showDialogModify) {
                    AlertDialog(
                        onDismissRequest = { viewModel.showDialogModify(false) },
                        title = { Text(text = "Modify Destination") },
                        text = {
                            Column {
                                Text("Name:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationName,
                                    onValueChange = { createDestinationName = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Description:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationDescription,
                                    onValueChange = { createDestinationDescription = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Country Mode:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationCountryMode,
                                    onValueChange = { createDestinationCountryMode = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Type:")
                                Spacer(modifier = Modifier.height(8.dp))
                                // DropdownMenu for selecting type
                                DropdownSelector(
                                    label = context.getString(R.string.typeLabel),
                                    options = listOf("City", "Country"),
                                    selectedOption = createDestinationType,
                                    onOptionSelected = { selectedType -> createDestinationType = selectedType }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Picture:")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = createDestinationPicture,
                                    onValueChange = { createDestinationPicture = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Last Modify:")
                                Spacer(modifier = Modifier.height(8.dp))
                                BasicTextField(
                                    value = DateUtils.formatDateFromMillis(createDestinationLastModify.millis),
                                    onValueChange = {
                                        createDestinationLastModify = Timestamp(it.toLong())
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    selectedRowIndex?.let { rowIndex ->
                                        viewModel.updateDestination(
                                            rowIndex,
                                            DestinationDomain(
                                                id = createDestinationId,
                                                createDestinationName,
                                                createDestinationDescription,
                                                createDestinationCountryMode,
                                                DestinationType.valueOf(createDestinationType), // Ensure correct enum value
                                                createDestinationPicture,
                                                Timestamp(createDestinationLastModify.millis)
                                            )
                                        )
                                        // Reset selected item and row variables
                                        viewModel.setSelectedDestinationIndex(null)
                                        viewModel.setSelectedDestination(
                                            DestinationDomain(
                                                id = "",
                                                name = "",
                                                description = "",
                                                countryMode = "",
                                                type = DestinationType.Country,
                                                picture = "",
                                                lastModify = Timestamp(0)
                                            )
                                        )
                                    }
                                    viewModel.showDialogModify(false)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryColor
                                )
                            ) {
                                Text("Modify")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { viewModel.showDialogModify(false) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                )
                            ) {
                                Text("Cancel")
                            }
                        }
                    )

                }

                // Delete Destination Dialog
                if (showDialogDelete) {
                    AlertDialog(
                        onDismissRequest = { viewModel.showDialogDelete(false) },
                        title = { Text(text = "Delete Destination") },
                        text = {
                            Text("Are you sure you want to delete selected destination?")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    selectedRowIndex?.let { rowIndex ->
                                        viewModel.deleteDestination(rowIndex)
                                        viewModel.setSelectedDestinationIndex(null)
                                        viewModel.setSelectedDestination(
                                            DestinationDomain(
                                                id = "",
                                                name = "",
                                                description = "",
                                                countryMode = "",
                                                type = DestinationType.Country,
                                                picture = "",
                                                lastModify = Timestamp(0)
                                            )
                                        )
                                    }
                                    viewModel.showDialogDelete(false)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryColor
                                )
                            ) {
                                Text("Delete")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { viewModel.showDialogDelete(false) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                )
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }

                /* THIS FUNTIONALLITY IS INCORPORATE FROM VERTICALDATASELECTOR
                // Search Result Dialog
                if (showDialogSearchResult) {
                    AlertDialog(
                        onDismissRequest = { showDialogSearchResult = false },
                        title = { Text(text = "Search Result") },
                        text = {
                            searchResultItem?.let { item ->
                                Column {
                                    Text("Name: ${item.name}")
                                    Text("Description: ${item.description}")
                                    Text("Country Mode: ${item.countryMode}")
                                    Text("Type: ${item.type}")
                                    Text("Picture: ${item.picture}")
                                    Text("Last Modify: ${item.lastModify?.millis}")
                                }
                            } ?: Text("Item not found")
                        },
                        confirmButton = {
                            Button(
                                onClick = { showDialogSearchResult = false }
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }
                */
            }
        }
    }
}
