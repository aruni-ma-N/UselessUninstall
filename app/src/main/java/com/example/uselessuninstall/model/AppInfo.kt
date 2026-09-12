package com.example.uselessuninstall.model

import android.graphics.drawable.Drawable

/**
 * UI representation of an installed application.
 *
 * This data class is decoupled from Android's [android.content.pm.PackageManager].
 * The backend developer will construct instances of this model and supply them to the UI.
 *
 * @property name User-facing display name of the application (e.g., "Instagram").
 * @property packageName Unique package identifier (e.g., "com.instagram.android").
 * @property iconDrawable Optional [Drawable] representing the app's icon.
 */
data class AppInfo(
    val name: String,
    val packageName: String,
    val iconDrawable: Drawable? = null
)

/**
 * Mock applications used for Compose Previews, UI testing, and standalone interactive flow.
 */
object SampleApps {
    val instagram = AppInfo(
        name = "Instagram",
        packageName = "com.instagram.android"
    )

    val spotify = AppInfo(
        name = "Spotify",
        packageName = "com.spotify.music"
    )

    val candyCrush = AppInfo(
        name = "Candy Crush Saga",
        packageName = "com.king.candycrushsaga"
    )

    val duolingo = AppInfo(
        name = "Duolingo",
        packageName = "com.duolingo"
    )

    val twitter = AppInfo(
        name = "X",
        packageName = "com.twitter.android"
    )

    val reddit = AppInfo(
        name = "Reddit",
        packageName = "com.reddit.frontpage"
    )

    val sampleList: List<AppInfo> = listOf(
        instagram,
        spotify,
        candyCrush,
        duolingo,
        twitter,
        reddit
    )
}
