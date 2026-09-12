package com.example.uselessuninstall.ui.state

import com.example.uselessuninstall.model.AppInfo

/**
 * Represents the distinct UI states of the Application Analyzer.
 *
 * Designed to be driven by a ViewModel or state coordinator without coupling to PackageManager.
 */
sealed interface UninstallerUiState {
    /**
     * Initial landing screen with diagnostic title, description, and "Initiate Deep Scan" action.
     */
    data object Home : UninstallerUiState

    /**
     * Theatrical multi-step analysis sequence screen ("Scanning installed applications...", etc.).
     *
     * @property app The candidate [AppInfo] selected for presentation upon analysis completion.
     */
    data class Analyzing(val app: AppInfo) : UninstallerUiState

    /**
     * Displaying the selected application candidate with options to "Proceed to Decommission" or "Keep It".
     */
    data class RandomApp(val app: AppInfo) : UninstallerUiState

    /**
     * Displaying the confirmation dialog: "Security Verification: Are you sure? 🤔".
     */
    data class Confirming(val app: AppInfo) : UninstallerUiState

    /**
     * Displaying the theatrical preparation and countdown screen before exposing final uninstall action.
     *
     * @property app The target app to be uninstalled.
     * @property secondsRemaining The current remaining seconds (default 3 down to 1).
     */
    data class Countdown(
        val app: AppInfo,
        val secondsRemaining: Int = 3
    ) : UninstallerUiState

    /**
     * Displaying the final result screen (either success or cancelled/failed).
     *
     * @property app The app that was targeted.
     * @property isSuccess True if uninstalled successfully, false if cancelled or failed.
     * @property message Optional descriptive message for the result.
     */
    data class Result(
        val app: AppInfo,
        val isSuccess: Boolean,
        val message: String? = null
    ) : UninstallerUiState
}
