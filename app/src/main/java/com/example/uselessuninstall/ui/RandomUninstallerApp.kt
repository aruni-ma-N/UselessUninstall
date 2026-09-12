package com.example.uselessuninstall.ui

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.uselessuninstall.model.AppInfo
import com.example.uselessuninstall.model.SampleApps
import com.example.uselessuninstall.ui.screens.CountdownScreen
import com.example.uselessuninstall.ui.screens.HomeScreen
import com.example.uselessuninstall.ui.screens.RandomAppScreen
import com.example.uselessuninstall.ui.screens.UninstallResultScreen
import com.example.uselessuninstall.ui.state.UninstallerUiState
import com.example.uselessuninstall.ui.theme.UselessUninstallTheme

/**
 * Top-level application coordinator for the Random App Uninstaller.
 *
 * Designed to be completely modular:
 * - Operates interactively in standalone mock mode using [SampleApps.sampleList].
 * - Allows another developer to supply real app selection and system uninstall hooks.
 *
 * @param modifier Modifier applied to the root container.
 * @param onSelectRandomApp Optional hook to supply a real randomly selected [AppInfo] from PackageManager.
 * @param onCountdownFinishedCallback Optional hook called when countdown finishes to trigger system uninstall.
 */
@Composable
fun RandomUninstallerApp(
    modifier: Modifier = Modifier,
    onSelectRandomApp: (() -> AppInfo?)? = null,
    onCountdownFinishedCallback: ((AppInfo) -> Unit)? = null
) {
    // Internal navigation state
    var uiState by remember {
        mutableStateOf<UninstallerUiState>(UninstallerUiState.Home)
    }

    // Helper to pick a random app (using hook if provided, or SampleApps pool)
    val pickRandomApp: () -> AppInfo = {
        onSelectRandomApp?.invoke() ?: SampleApps.sampleList.random()
    }

    RandomUninstallerContent(
        uiState = uiState,
        modifier = modifier,
        onFindRandomApp = {
            uiState = UninstallerUiState.RandomApp(pickRandomApp())
        },
        onUninstallClick = { app ->
            uiState = UninstallerUiState.Confirming(app)
        },
        onKeepClick = {
            uiState = UninstallerUiState.Home
        },
        onConfirmUninstall = { app ->
            uiState = UninstallerUiState.Countdown(app = app, secondsRemaining = 5)
        },
        onDismissConfirmation = { app ->
            uiState = UninstallerUiState.RandomApp(app)
        },
        onCountdownFinished = { app ->
            // Invoke optional backend callback for system uninstall
            onCountdownFinishedCallback?.invoke(app)

            // Transition UI to result screen (simulated success in UI mock mode)
            uiState = UninstallerUiState.Result(app = app, isSuccess = true)
        },
        onCancelCountdown = { app ->
            // Cancelling only stops the countdown and returns to the app screen
            uiState = UninstallerUiState.RandomApp(app)
        },
        onFindAnother = {
            uiState = UninstallerUiState.RandomApp(pickRandomApp())
        },
        onBackHome = {
            uiState = UninstallerUiState.Home
        },
        onTryAgain = { app ->
            uiState = UninstallerUiState.RandomApp(app)
        }
    )
}

/**
 * Pure, stateless screen dispatcher based on [UninstallerUiState].
 *
 * Allows another developer to drive the entire flow using a ViewModel if preferred.
 */
@Composable
fun RandomUninstallerContent(
    uiState: UninstallerUiState,
    modifier: Modifier = Modifier,
    onFindRandomApp: () -> Unit,
    onUninstallClick: (AppInfo) -> Unit,
    onKeepClick: (AppInfo) -> Unit,
    onConfirmUninstall: (AppInfo) -> Unit,
    onDismissConfirmation: (AppInfo) -> Unit,
    onCountdownFinished: (AppInfo) -> Unit,
    onCancelCountdown: (AppInfo) -> Unit,
    onFindAnother: () -> Unit,
    onBackHome: () -> Unit,
    onTryAgain: (AppInfo) -> Unit
) {
    Crossfade(
        targetState = uiState,
        label = "ScreenTransition",
        modifier = modifier
    ) { state ->
        when (state) {
            is UninstallerUiState.Home -> {
                HomeScreen(
                    onFindRandomApp = onFindRandomApp
                )
            }

            is UninstallerUiState.RandomApp -> {
                RandomAppScreen(
                    app = state.app,
                    onUninstall = { onUninstallClick(state.app) },
                    onKeep = { onKeepClick(state.app) },
                    showConfirmationDialog = false
                )
            }

            is UninstallerUiState.Confirming -> {
                RandomAppScreen(
                    app = state.app,
                    onUninstall = { onUninstallClick(state.app) },
                    onKeep = { onKeepClick(state.app) },
                    showConfirmationDialog = true,
                    onConfirmUninstall = { onConfirmUninstall(state.app) },
                    onDismissConfirmation = { onDismissConfirmation(state.app) }
                )
            }

            is UninstallerUiState.Countdown -> {
                CountdownScreen(
                    app = state.app,
                    initialSeconds = state.secondsRemaining,
                    onCountdownFinished = { onCountdownFinished(state.app) },
                    onCancel = { onCancelCountdown(state.app) }
                )
            }

            is UninstallerUiState.Result -> {
                UninstallResultScreen(
                    app = state.app,
                    isSuccess = state.isSuccess,
                    message = state.message,
                    onFindAnother = onFindAnother,
                    onBackHome = onBackHome,
                    onTryAgain = { onTryAgain(state.app) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RandomUninstallerAppPreview() {
    UselessUninstallTheme {
        RandomUninstallerApp()
    }
}
