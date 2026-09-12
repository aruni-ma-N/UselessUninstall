package com.example.uselessuninstall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.uselessuninstall.ui.RandomUninstallerApp
import com.example.uselessuninstall.ui.theme.UselessUninstallTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UselessUninstallTheme {
                // To connect PackageManager and Android uninstall logic later:
                // 1. Supply `onSelectRandomApp` to pick from the real PackageManager installed list.
                // 2. Supply `onCountdownFinishedCallback` to launch Android's system uninstall intent:
                //    val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE).apply {
                //        data = Uri.parse("package:${app.packageName}")
                //        putExtra(Intent.EXTRA_RETURN_RESULT, true)
                //    }
                //    uninstallLauncher.launch(intent)
                RandomUninstallerApp(
                    onSelectRandomApp = null, // uses SampleApps mock pool by default
                    onCountdownFinishedCallback = null // UI simulation by default
                )
            }
        }
    }
}