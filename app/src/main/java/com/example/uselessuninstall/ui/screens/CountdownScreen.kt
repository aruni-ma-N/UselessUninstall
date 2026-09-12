package com.example.uselessuninstall.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uselessuninstall.model.AppInfo
import com.example.uselessuninstall.model.SampleApps
import com.example.uselessuninstall.ui.components.AppIconDisplay
import com.example.uselessuninstall.ui.components.CountdownNumber
import com.example.uselessuninstall.ui.theme.UselessUninstallTheme
import kotlinx.coroutines.delay

/**
 * Screen 4: Full-screen animated countdown screen from 5 to 1.
 *
 * NOTE: When the countdown completes, it triggers [onCountdownFinished]. It does NOT execute any
 * Android uninstall operation. The backend developer will connect this callback.
 *
 * @param app The [AppInfo] of the app about to be uninstalled.
 * @param onCountdownFinished Triggered when the countdown reaches 0.
 * @param onCancel Triggered if the user cancels the countdown timer.
 * @param modifier Modifier applied to the screen root.
 * @param initialSeconds Starting countdown duration (default is 5).
 * @param autoCountdown True to run the countdown timer automatically; false for previewing static numbers.
 */
@Composable
fun CountdownScreen(
    app: AppInfo,
    onCountdownFinished: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    initialSeconds: Int = 5,
    autoCountdown: Boolean = true
) {
    var secondsRemaining by rememberSaveable { mutableIntStateOf(initialSeconds) }

    if (autoCountdown) {
        LaunchedEffect(secondsRemaining) {
            if (secondsRemaining > 1) {
                delay(1000L)
                secondsRemaining -= 1
            } else if (secondsRemaining == 1) {
                delay(1000L)
                onCountdownFinished()
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // App target header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    AppIconDisplay(app = app, size = 36.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = app.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Uninstalling in...",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Visual Focus: Center Animated Countdown Number
            CountdownNumber(
                number = secondsRemaining,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            // Cancel section & disclaimer
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Cancel Countdown",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Cancelling only stops this timer and returns to app selection.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CountdownScreenPreview5() {
    UselessUninstallTheme {
        CountdownScreen(
            app = SampleApps.instagram,
            onCountdownFinished = {},
            onCancel = {},
            initialSeconds = 5,
            autoCountdown = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CountdownScreenPreview1() {
    UselessUninstallTheme {
        CountdownScreen(
            app = SampleApps.instagram,
            onCountdownFinished = {},
            onCancel = {},
            initialSeconds = 1,
            autoCountdown = false
        )
    }
}
