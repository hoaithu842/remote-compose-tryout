package io.github.hoaithu842.rachel.remotecompose.controller.composables

import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

val BUTTON_COLOR = Color(0xFF00BCD4)

val ButtonSpec = RemoteSpec(
    fileName = "composable_button.rc",
    content = { RemoteButtonComposable() },
)

@Preview
@Composable
fun RemoteButtonComposable() {
    RemoteColumn(
        modifier = RemoteModifier.fillMaxSize(),
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteText(
            text = "Click Me",
            color = RemoteColor(BUTTON_COLOR)
        )
    }
}