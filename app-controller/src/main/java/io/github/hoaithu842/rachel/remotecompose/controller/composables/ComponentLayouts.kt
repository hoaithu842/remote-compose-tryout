package io.github.hoaithu842.rachel.remotecompose.controller.composables

import android.annotation.SuppressLint
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteFlowRow
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteSpacer
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.RemoteDp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hoaithu842.rachel.remotecompose.controller.RemoteSpec

val LayoutsSpec = RemoteSpec(
    fileName = "composable_layouts.rc",
    content = { ComponentLayouts() },
)

@SuppressLint("RestrictedApiAndroidX", "RestrictedApi")
@Preview
@Composable
fun ComponentLayouts() {
    RemoteColumn(
        modifier = RemoteModifier.fillMaxSize().padding(16.dp),
        verticalArrangement = RemoteArrangement.SpaceEvenly,
    ) {
        // ── RemoteRow ────────────────────────────────────────────
        RemoteText("RemoteRow – SpaceBetween", color = RemoteColor(Color.DarkGray))
        RemoteRow(
            modifier = RemoteModifier
                .background(Color(0xFFEEEEEE))
                .padding(8.dp),
            horizontalArrangement = RemoteArrangement.SpaceBetween,
            verticalAlignment = RemoteAlignment.CenterVertically,
        ) {
            listOf(Color.Red, Color.Green, Color.Blue).forEach { color ->
                RemoteBox(
                    modifier = RemoteModifier
                        .size(RemoteDp(40.dp))
                        .background(color),
                )
            }
        }

        // ── RemoteColumn ─────────────────────────────────────────
        RemoteText("RemoteColumn – Center", color = RemoteColor(Color.DarkGray))
        RemoteColumn(
            modifier = RemoteModifier
                .background(Color(0xFFEEEEEE))
                .padding(8.dp),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.SpaceEvenly,
        ) {
            listOf("Alpha", "Beta", "Gamma").forEach { label ->
                RemoteText(label, color = RemoteColor(Color(0xFF6200EE)))
            }
        }

        // ── RemoteBox ─────────────────────────────────────────────
        RemoteText("RemoteBox – stacked layers", color = RemoteColor(Color.DarkGray))
        RemoteBox(
            modifier = RemoteModifier
                .size(RemoteDp(80.dp))
                .background(Color(0xFFBB86FC)),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText("Front", color = RemoteColor(Color.White))
        }

        // ── RemoteSpacer ──────────────────────────────────────────
        RemoteText("RemoteSpacer (16 dp gap below):", color = RemoteColor(Color.DarkGray))
        RemoteSpacer(modifier = RemoteModifier.size(RemoteDp(16.dp)))

        // ── RemoteFlowRow ─────────────────────────────────────────
        RemoteText("RemoteFlowRow – wrapping chips", color = RemoteColor(Color.DarkGray))
        RemoteFlowRow(
            modifier = RemoteModifier
                .background(Color(0xFFEEEEEE))
                .padding(8.dp),
            horizontalArrangement = RemoteArrangement.SpaceEvenly,
            verticalArrangement = RemoteArrangement.SpaceEvenly,
        ) {
            listOf("Compose", "Remote", "Android", "Kotlin", "UI", "Server").forEach { tag ->
                RemoteBox(
                    modifier = RemoteModifier
                        .background(Color(0xFF6200EE))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                ) {
                    RemoteText(tag, color = RemoteColor(Color.White))
                }
            }
        }
    }
}
