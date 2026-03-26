package io.github.hoaithu842.rachel.remotecompose.controller.composables

import android.annotation.SuppressLint
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.shaders.RemoteBrush
import androidx.compose.remote.creation.compose.shaders.horizontalGradient
import androidx.compose.remote.creation.compose.shaders.linearGradient
import androidx.compose.remote.creation.compose.shaders.verticalGradient
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.RemoteDp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val GradientsSpec = RemoteSpec(
    fileName = "composable_gradients.rc",
    content = { ComponentGradients() },
)

@SuppressLint("RestrictedApiAndroidX", "RestrictedApi")
@Preview
@Composable
fun ComponentGradients() {
    RemoteColumn(
        modifier = RemoteModifier.fillMaxSize().padding(16.dp),
        verticalArrangement = RemoteArrangement.SpaceEvenly,
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
    ) {
        RemoteText("linearGradient (diagonal)", color = RemoteColor(Color.DarkGray))
        RemoteBox(
            modifier = RemoteModifier
                .size(RemoteDp(240.dp), RemoteDp(80.dp))
                .background(
                    RemoteBrush.linearGradient(
                        colors = listOf(
                            RemoteColor(Color(0xFF6200EE)),
                            RemoteColor(Color(0xFF03DAC6))
                        ),
                    )
                ),
        )

        RemoteText("horizontalGradient", color = RemoteColor(Color.DarkGray))
        RemoteBox(
            modifier = RemoteModifier
                .size(RemoteDp(240.dp), RemoteDp(80.dp))
                .background(
                    RemoteBrush.horizontalGradient(
                        colors = listOf(
                            RemoteColor(Color.Red), RemoteColor(Color.Yellow),
                            RemoteColor(Color.Green)
                        ),
                    )
                ),
        )

        RemoteText("verticalGradient", color = RemoteColor(Color.DarkGray))
        RemoteBox(
            modifier = RemoteModifier
                .size(RemoteDp(240.dp), RemoteDp(80.dp))
                .background(
                    RemoteBrush.verticalGradient(
                        colors = listOf(
                            RemoteColor(Color(0xFF0D47A1)), RemoteColor(Color(0xFF42A5F5)),
                            RemoteColor(Color.White)
                        ),
                    )
                ),
        )

        RemoteText("Gradient + text overlay", color = RemoteColor(Color.DarkGray))
        RemoteBox(
            modifier = RemoteModifier
                .size(RemoteDp(240.dp), RemoteDp(80.dp))
                .background(
                    RemoteBrush.linearGradient(
                        colors = listOf(
                            RemoteColor(Color(0xFFE91E63)),
                            RemoteColor(Color(0xFFFF9800))
                        ),
                    )
                ),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText("Hello Gradient", color = RemoteColor(Color.White))
        }
    }
}
