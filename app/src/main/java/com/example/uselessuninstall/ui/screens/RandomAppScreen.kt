package com.example.uselessuninstall.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uselessuninstall.model.AppInfo
import com.example.uselessuninstall.model.SampleApps
import com.example.uselessuninstall.ui.components.AppInfoCard
import com.example.uselessuninstall.ui.components.UninstallConfirmationDialog
import com.example.uselessuninstall.ui.theme.UselessUninstallTheme

/**
 * Screen 2: Displays the randomly selected application and asks whether to uninstall or keep it.
 *
 * @param app The [AppInfo] of the randomly selected application.
 * @param onUninstall Callback when the user taps "Uninstall" (triggers confirmation dialog).
 * @param onKeep Callback when the user taps "Keep It" (returns home or finds another).
 * @param modifier Modifier applied to the screen root.
 * @param showConfirmationDialog If true, renders [UninstallConfirmationDialog] over this screen.
 * @param onConfirmUninstall Callback when the user confirms "Yes, Uninstall" in the dialog.
 * @param onDismissConfirmation Callback when the user cancels the confirmation dialog.
 */
@Composable
fun RandomAppScreen(
    app: AppInfo,
    onUninstall: () -> Unit,
    onKeep: () -> Unit,
    modifier: Modifier = Modifier,
    showConfirmationDialog: Boolean = false,
    onConfirmUninstall: () -> Unit = {},
    onDismissConfirmation: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header / Question
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎯 Fate Has Decided!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "How about uninstalling this app?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }

            // Selected App Card (Center)
            AppInfoCard(
                app = app,
                isCentered = true,
                iconSize = 88.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Bottom Buttons: "Uninstall" & "Keep It"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Destructive / high emphasis "Uninstall" button
                Button(
                    onClick = onUninstall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = "Uninstall",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Secondary "Keep It" button
                OutlinedButton(
                    onClick = onKeep,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(
                        text = "Keep It",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    // Confirmation Dialog Overlay (Screen 3)
    if (showConfirmationDialog) {
        UninstallConfirmationDialog(
            app = app,
            onConfirmUninstall = onConfirmUninstall,
            onCancel = onDismissConfirmation
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RandomAppScreenPreview() {
    UselessUninstallTheme {
        RandomAppScreen(
            app = SampleApps.instagram,
            onUninstall = {},
            onKeep = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RandomAppScreenWithDialogPreview() {
    UselessUninstallTheme {
        RandomAppScreen(
            app = SampleApps.instagram,
            onUninstall = {},
            onKeep = {},
            showConfirmationDialog = true
        )
    }
}
