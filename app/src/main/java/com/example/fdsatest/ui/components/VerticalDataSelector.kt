package com.example.fdsatest.ui.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.fdsatest.R
import com.example.fdsatest.ui.viewmodel.SharedViewModel
import java.util.*

@Composable
fun VerticalDataSelector(
    context: Context,
    modifier: Modifier = Modifier,
    data: List<String>,
    onSearchPerformed: (List<Int>) -> Unit, // Change to pass a list of matching indices
    viewModel: SharedViewModel
) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it

                    val searchTextLower = searchText.lowercase(Locale.ROOT).trim()
                    val matchingIndices = data.mapIndexedNotNull { index, item ->
                        if (item.lowercase(Locale.ROOT).contains(searchTextLower)) index else null
                    }

                    if (matchingIndices.isNotEmpty()) {
                        onSearchPerformed(matchingIndices) // Pass all matching indices
                    } else {
                        onSearchPerformed(emptyList()) // No matches
                        viewModel.showError(context.getString(R.string.no_data_found))
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = { Text(stringResource(R.string.search_name)) },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_manage_search_24),
                        contentDescription = "Search icon"
                    )
                }
            )
        }
    }
}
