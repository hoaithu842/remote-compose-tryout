package io.github.hoaithu842.rachel.remotecompose.controller.composables

import android.annotation.SuppressLint
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.border
import androidx.compose.remote.creation.compose.modifier.clip
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.graphicsLayer
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.shapes.RemoteCircleShape
import androidx.compose.remote.creation.compose.shapes.RemoteRoundedCornerShape
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.RemoteDp
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hoaithu842.rachel.remotecompose.controller.RemoteSpec

val ModifiersSpec = RemoteSpec(
    fileName = "composable_modifiers.rc",
    content = { ComponentModifiers() },
)

@SuppressLint("RestrictedApiAndroidX", "RestrictedApi")
@Preview
@Composable
fun ComponentModifiers() {
    RemoteColumn(
        modifier = RemoteModifier.fillMaxSize().padding(16.dp),
        verticalArrangement = RemoteArrangement.SpaceEvenly,
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
    ) {
        // ── background + padding ──────────────────────────────────
        RemoteText("background + padding", color = RemoteColor(Color.DarkGray))
        RemoteBox(
            modifier = RemoteModifier
                .background(Color(0xFF6200EE))
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            RemoteText("Padded Box", color = RemoteColor(Color.White))
        }

        // ── border ────────────────────────────────────────────────
        RemoteText("border (rounded)", color = RemoteColor(Color.DarkGray))
        RemoteBox(
            modifier = RemoteModifier
                .size(RemoteDp(120.dp), RemoteDp(48.dp))
                .border(
                    RemoteDp(2.dp),
                    RemoteColor(Color(0xFF6200EE)),
                    RemoteRoundedCornerShape(RemoteDp(8.dp))
                ),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText("Outlined", color = RemoteColor(Color(0xFF6200EE)))
        }

        // ── clip – rounded corners ────────────────────────────────
        RemoteText("clip – RoundedCornerShape", color = RemoteColor(Color.DarkGray))
        RemoteBox(
            modifier = RemoteModifier
                .size(RemoteDp(120.dp), RemoteDp(48.dp))
                .clip(RemoteRoundedCornerShape(RemoteDp(16.dp)))
                .background(Color(0xFF03DAC6)),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText("Rounded", color = RemoteColor(Color.Black))
        }

        // ── clip – circle ─────────────────────────────────────────
        RemoteText("clip – CircleShape", color = RemoteColor(Color.DarkGray))
        RemoteBox(
            modifier = RemoteModifier
                .size(RemoteDp(64.dp))
                .clip(RemoteCircleShape)
                .background(Color(0xFFE91E63)),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText("●", color = RemoteColor(Color.White))
        }

        // ── graphicsLayer – rotation ──────────────────────────────
        RemoteText("graphicsLayer – rotationZ 15°", color = RemoteColor(Color.DarkGray))
        RemoteBox(
            modifier = RemoteModifier
                .size(RemoteDp(100.dp), RemoteDp(40.dp))
                .background(Color(0xFFFF9800))
                .graphicsLayer(rotationZ = 15f.rf),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText("Rotated", color = RemoteColor(Color.White))
        }

        // ── graphicsLayer – scale ─────────────────────────────────
        RemoteText("graphicsLayer – scale 1.3x", color = RemoteColor(Color.DarkGray))
        RemoteBox(
            modifier = RemoteModifier
                .size(RemoteDp(80.dp), RemoteDp(36.dp))
                .background(Color(0xFF4CAF50))
                .graphicsLayer(scaleX = 1.3f.rf, scaleY = 1.3f.rf),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText("Scaled", color = RemoteColor(Color.White))
        }

        // ── graphicsLayer – alpha ─────────────────────────────────
        RemoteText("graphicsLayer – alpha 0.4", color = RemoteColor(Color.DarkGray))
        RemoteBox(
            modifier = RemoteModifier
                .size(RemoteDp(100.dp), RemoteDp(36.dp))
                .background(Color(0xFF9C27B0))
                .graphicsLayer(alpha = 0.4f.rf),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText("Faded", color = RemoteColor(Color.White))
        }

        // ── graphicsLayer – shadow elevation ─────────────────────
        RemoteText("graphicsLayer – shadow elevation 8", color = RemoteColor(Color.DarkGray))
        RemoteBox(
            modifier = RemoteModifier
                .size(RemoteDp(100.dp), RemoteDp(40.dp))
                .clip(RemoteRoundedCornerShape(RemoteDp(8.dp)))
                .background(Color.White)
                .graphicsLayer(shadowElevation = 8f.rf),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText("Shadow", color = RemoteColor(Color.Black))
        }
    }
}
