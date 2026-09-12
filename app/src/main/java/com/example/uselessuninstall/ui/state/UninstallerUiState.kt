package com.example.uselessuninstall.ui.state

import com.example.uselessuninstall.model.AppInfo

/**
 * Represents the distinct UI states of the Random App Uninstaller.
 *
 * Designed to be driven by a ViewModel or state coordinator without coupling to PackageManager.
 */
sealed interface UninstallerUiState {
    /**
     * Initial landing screen with title, description, and "🎲 Find a Random App" action.
     */
    data object Home : UninstallerUiState

    /**
     * Displaying the randomly selected application with options to "Uninstall" or "Keep It".
     */
    data class RandomApp(val app: AppInfo) : UninstallerUiState

    /**
     * Displaying the confirmation dialog: "Are you sure? You are about to uninstall [App Name]".
     */
    data class Confirming(val app: AppInfo) : UninstallerUiState

    /**
     * Displaying the 5-second animated countdown screen before uninstall.
     *
     * @property app The target app to be uninstalled.
     * @property secondsRemaining The current remaining seconds (e.g. 5 down to 1).
     */
    data class Countdown(
        val app: AppInfo,
        val secondsRemaining: Int = 5
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
