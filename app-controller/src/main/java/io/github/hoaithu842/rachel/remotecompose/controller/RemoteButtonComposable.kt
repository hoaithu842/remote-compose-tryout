package io.github.hoaithu842.rachel.remotecompose.controller

import android.content.Context
import androidx.compose.remote.creation.compose.capture.captureSingleRemoteDocument
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

// ╔═══════════════════════════════════════════════════════════╗
// ║  Change BUTTON_COLOR, rebuild, tap "Generate" in the     ║
// ║  controller app. The .rc file is saved straight into      ║
// ║  RachelRemoteComposeServer/documents/ for the server to   ║
// ║  pick up.                                                 ║
// ╚═══════════════════════════════════════════════════════════╝
val BUTTON_COLOR = Color(0xFF00BCD4) // <- change this

@Preview
@Composable
fun RemoteButtonComposable() {
    RemoteColumn(
        modifier = RemoteModifier.fillMaxSize(),
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteText(text = "Click Me", color = RemoteColor(BUTTON_COLOR))
    }
}

suspend fun generateDocument(context: Context): ByteArray {
    return captureSingleRemoteDocument(
        context = context,
        content = { RemoteButtonComposable() },
    ).bytes
}
