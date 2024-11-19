package com.example.fdsatest.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.fdsatest.ui.viewmodel.SharedViewModel
import com.example.fdsatest.data.remote.models.DestinationType
import com.example.fdsatest.domain.models.DestinationDomain
import com.example.fdsatest.ui.nav.Screens
import com.example.fdsatest.utils.DateUtils

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditableTable(
    data: List<DestinationDomain?>,
    onCellSelected: (rowIndex: Int) -> Unit,
    selectedRowIndex: MutableState<Int?>,
    isModifyMode: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SharedViewModel
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            onRefresh()
            isRefreshing = false
        }
    )
    val headers = listOf(
        "ID (int)",
        "Name (string)",
        "Description (string)",
        "CountryCode (string)",
        "Type (enum)",
        "Picture (string)",
        "LastModify (DateTime)"
    )

    Box(modifier = modifier.pullRefresh(pullRefreshState)) {
        Column(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            // Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp)
            ) {
                headers.forEach { header ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .width(150.dp)
                    ) {
                        Text(
                            text = header,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }
                }
            }
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)

            // Scrollable content for data rows within pull-to-refresh
            LazyColumn {
                itemsIndexed(
                    data,
                    key = { index, item -> item?.id ?: index.toString() }) { rowIndex, rowData ->
                    val isSelected = rowIndex == selectedRowIndex.value

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            ) // Add spacing around the card
                            .clickable {
                                if (!isModifyMode) {
                                    onCellSelected(rowIndex)
                                }
                            },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color.LightGray.copy(alpha = 1f) else Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp) // Padding inside the card
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = {
                                            viewModel.setSelectedDestination(
                                                destination = DestinationDomain(
                                                    rowData?.id ?: "",
                                                    rowData?.name ?: "",
                                                    rowData?.description ?: "",
                                                    rowData?.countryMode ?: "",
                                                    rowData?.type ?: DestinationType.Country,
                                                    rowData?.picture ?: "",
                                                    rowData?.lastModify
                                                )
                                            )
                                            navController.navigate(Screens.DetailScreen.route)
                                        },
                                        onPress = {
                                            selectedRowIndex.value = rowIndex
                                            viewModel.setSelectedDestinationIndex(rowIndex)
                                        }
                                    )
                                }
                        ) {
                            listOf(
                                rowData?.id ?: "",
                                rowData?.name ?: "",
                                rowData?.description ?: "",
                                rowData?.countryMode ?: "",
                                rowData?.type?.name ?: "",
                                rowData?.picture ?: "",
                                DateUtils.formatDateFromMillis(rowData?.lastModify?.millis ?: 0L)
                            ).forEachIndexed { colIndex, value ->
                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .width(150.dp)
                                ) {

                                    Text(
                                        text = value,
                                        modifier = Modifier.fillMaxWidth(),
                                        color = if (colIndex == 0) Color.Gray else Color.Black
                                    )
                                }

                                if (colIndex < headers.size - 1) {
                                    HorizontalDivider(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(1.dp),
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
