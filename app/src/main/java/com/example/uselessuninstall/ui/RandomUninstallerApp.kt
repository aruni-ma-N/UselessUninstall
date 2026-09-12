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
import com.example.uselessuninstall.ui.screens.AnalysisScreen
import com.example.uselessuninstall.ui.screens.CountdownScreen
import com.example.uselessuninstall.ui.screens.HomeScreen
import com.example.uselessuninstall.ui.screens.RandomAppScreen
import com.example.uselessuninstall.ui.screens.UninstallResultScreen
import com.example.uselessuninstall.ui.state.UninstallerUiState
import com.example.uselessuninstall.ui.theme.UselessUninstallTheme

/**
 * Top-level application coordinator for the Automated Application Analyzer.
 *
 * Simulates a serious diagnostic system while secretly driving random decommissioning.
 *
 * @param modifier Modifier applied to the root container.
 * @param onSelectRandomApp Optional hook to supply a real [AppInfo] (will later connect to RandomAppSelector).
 * @param onCountdownFinishedCallback Optional hook called upon final action authorization (will later connect to UninstallManager).
 */
@Composable
fun RandomUninstallerApp(
    modifier: Modifier = Modifier,
    onSelectRandomApp: (() -> AppInfo?)? = null,
    onCountdownFinishedCallback: ((AppInfo) -> Unit)? = null
) {
    var uiState by remember {
        mutableStateOf<UninstallerUiState>(UninstallerUiState.Home)
    }

    // =========================================================================
    // PLACEHOLDER: App candidate selection
    // Currently resolves via onSelectRandomApp hook or falls back to SampleApps mock pool.
    // In future integration, this will be wired to real AppInfo from InstalledAppRetriever
    // chosen by RandomAppSelector.
    // =========================================================================
    val pickCandidateApp: () -> AppInfo = {
        onSelectRandomApp?.invoke() ?: SampleApps.sampleList.random()
    }

    RandomUninstallerContent(
        uiState = uiState,
        modifier = modifier,
        onInitiateAnalysis = {
            // Initiate the serious diagnostic analysis sequence
            val candidate = pickCandidateApp()
            uiState = UninstallerUiState.Analyzing(candidate)
        },
        onAnalysisComplete = { candidate ->
            // Transition to Candidate Selected screen once diagnostic sequence completes
            uiState = UninstallerUiState.RandomApp(candidate)
        },
        onCancelAnalysis = {
            uiState = UninstallerUiState.Home
        },
        onUninstallClick = { app ->
            uiState = UninstallerUiState.Confirming(app)
        },
        onKeepClick = {
            uiState = UninstallerUiState.Home
        },
        onConfirmUninstall = { app ->
            // Proceed to theatrical preparation and countdown
            uiState = UninstallerUiState.Countdown(app = app, secondsRemaining = 3)
        },
        onDismissConfirmation = { app ->
            uiState = UninstallerUiState.RandomApp(app)
        },
        onExecuteFinalUninstall = { app ->
            // =====================================================================
            // PLACEHOLDER: Final Uninstall Action
            // Invoked when user confirms the final action button after the countdown.
            // In future integration, this will call UninstallManager.requestUninstall().
            // =====================================================================
            onCountdownFinishedCallback?.invoke(app)
            uiState = UninstallerUiState.Result(app = app, isSuccess = true)
        },
        onCancelCountdown = { app ->
            uiState = UninstallerUiState.RandomApp(app)
        },
        onFindAnother = {
            val candidate = pickCandidateApp()
            uiState = UninstallerUiState.Analyzing(candidate)
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
 */
@Composable
fun RandomUninstallerContent(
    uiState: UninstallerUiState,
    modifier: Modifier = Modifier,
    onInitiateAnalysis: () -> Unit,
    onAnalysisComplete: (AppInfo) -> Unit,
    onCancelAnalysis: () -> Unit,
    onUninstallClick: (AppInfo) -> Unit,
    onKeepClick: (AppInfo) -> Unit,
    onConfirmUninstall: (AppInfo) -> Unit,
    onDismissConfirmation: (AppInfo) -> Unit,
    onExecuteFinalUninstall: (AppInfo) -> Unit,
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
                    onFindRandomApp = onInitiateAnalysis
                )
            }

            is UninstallerUiState.Analyzing -> {
                AnalysisScreen(
                    onAnalysisComplete = { onAnalysisComplete(state.app) },
                    onCancel = onCancelAnalysis
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
                    onExecuteUninstall = { onExecuteFinalUninstall(state.app) },
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
