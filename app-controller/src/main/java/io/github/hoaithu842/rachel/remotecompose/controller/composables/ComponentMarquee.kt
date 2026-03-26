package io.github.hoaithu842.rachel.remotecompose.controller.composables

import android.annotation.SuppressLint
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.basicMarquee
import androidx.compose.remote.creation.compose.modifier.clip
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.shapes.RemoteRoundedCornerShape
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.RemoteDp
import androidx.compose.remote.creation.compose.state.asRemoteTextUnit
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.hoaithu842.rachel.remotecompose.controller.RemoteSpec

val MarqueeSpec = RemoteSpec(
    fileName = "composable_marquee.rc",
    content = { ComponentMarquee() },
)

@SuppressLint("RestrictedApiAndroidX", "RestrictedApi")
@Preview
@Composable
fun ComponentMarquee() {
    RemoteColumn(
        modifier = RemoteModifier.fillMaxSize().padding(16.dp),
        verticalArrangement = RemoteArrangement.SpaceEvenly,
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
    ) {
        RemoteText(
            "basicMarquee – scrolling text",
            fontSize = 18.sp.asRemoteTextUnit(),
            color = RemoteColor(Color(0xFF6200EE))
        )

        // ── Default marquee ──────────────────────────────────────
        RemoteBox(
            modifier = RemoteModifier
                .size(RemoteDp(260.dp), RemoteDp(40.dp))
                .clip(RemoteRoundedCornerShape(RemoteDp(8.dp)))
                .background(Color(0xFFEDE7F6))
                .padding(horizontal = 8.dp),
        ) {
            RemoteText(
                "Breaking news: Remote Compose enables server-driven UI without app updates!",
                modifier = RemoteModifier.basicMarquee(iterations = Int.MAX_VALUE, velocity = 40f),
                color = RemoteColor(Color(0xFF6200EE)),
                maxLines = 1,
            )
        }

        // ── Slower marquee ───────────────────────────────────────
        RemoteBox(
            modifier = RemoteModifier
                .size(RemoteDp(260.dp), RemoteDp(40.dp))
                .clip(RemoteRoundedCornerShape(RemoteDp(8.dp)))
                .background(Color(0xFF03DAC6))
                .padding(horizontal = 8.dp),
        ) {
            RemoteText(
                "Slow scroll: 🌍  Earth  ·  🌕  Moon  ·  ☀️  Sun  ·  ⭐  Stars  ·  🚀  Space",
                modifier = RemoteModifier.basicMarquee(iterations = Int.MAX_VALUE, velocity = 20f),
                color = RemoteColor(Color.Black),
                maxLines = 1,
            )
        }

        // ── Marquee with delay ───────────────────────────────────
        RemoteBox(
            modifier = RemoteModifier
                .size(RemoteDp(260.dp), RemoteDp(40.dp))
                .clip(RemoteRoundedCornerShape(RemoteDp(8.dp)))
                .background(Color(0xFFE91E63))
                .padding(horizontal = 8.dp),
        ) {
            RemoteText(
                "Delayed start (1 s): This text waits before scrolling across the screen.",
                modifier = RemoteModifier.basicMarquee(
                    iterations = Int.MAX_VALUE,
                    velocity = 35f,
                    initialDelayMillis = 1000f,
                ),
                color = RemoteColor(Color.White),
                maxLines = 1,
            )
        }
    }
}
