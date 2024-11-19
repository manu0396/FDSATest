package com.example.fdsatest.ui.components

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fdsatest.R
import com.example.fdsatest.ui.theme.PrimaryColor

@Composable
fun SimpleAlertDialog(
    context: Context,
    show: Boolean,
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    elevation: Int,
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(
                        text = context.getString(R.string.ok),
                        color = PrimaryColor
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(
                        context.getString(R.string.cancel),
                        color = Color.Red
                    )
                }
            },
            title = { Text(title) },
            text = { Text(text) },
            tonalElevation = elevation.dp
        )
    }
}