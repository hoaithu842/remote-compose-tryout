package io.github.hoaithu842.rachel.remotecompose.controller.usecases

import android.annotation.SuppressLint
import androidx.compose.remote.creation.compose.action.HostAction
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.clickable
import androidx.compose.remote.creation.compose.modifier.clip
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.shapes.RemoteRoundedCornerShape
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.RemoteDp
import androidx.compose.remote.creation.compose.state.asRemoteTextUnit
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.hoaithu842.rachel.remotecompose.controller.RemoteSpec

val UseCaseNavigationSpec =
    RemoteSpec(
        fileName = "usecase_navigation.rc",
        content = { UseCaseNavigation() },
    )

private const val ACTION_NAVIGATE_PROFILE = 4001
private const val ACTION_NAVIGATE_SETTINGS = 4002

@SuppressLint("RestrictedApiAndroidX", "RestrictedApi")
@Preview
@Composable
fun UseCaseNavigation() {
    RemoteColumn(
        modifier = RemoteModifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(32.dp),
        verticalArrangement = RemoteArrangement.Center,
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
    ) {
        RemoteText(
            "Home",
            fontSize = 28.sp.asRemoteTextUnit(),
            color = RemoteColor(Color(0xFF1A1A2E)),
        )
        RemoteText(
            "Choose a destination",
            fontSize = 14.sp.asRemoteTextUnit(),
            color = RemoteColor(Color.Gray),
        )

        // ── Navigate to Profile ────────────────────────────
        RemoteBox(
            modifier = RemoteModifier
                .clip(RemoteRoundedCornerShape(RemoteDp(12.dp)))
                .background(Color(0xFF6200EE))
                .padding(horizontal = 48.dp, vertical = 16.dp)
                .clickable(HostAction(name = "navigate_profile".rs, id = ACTION_NAVIGATE_PROFILE)),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText(
                "Navigate to Profile",
                fontSize = 16.sp.asRemoteTextUnit(),
                color = RemoteColor(Color.White),
            )
        }

        // ── Navigate to Settings ───────────────────────────
        RemoteBox(
            modifier = RemoteModifier
                .clip(RemoteRoundedCornerShape(RemoteDp(12.dp)))
                .background(Color(0xFF00897B))
                .padding(horizontal = 48.dp, vertical = 16.dp)
                .clickable(
                    HostAction(
                        name = "navigate_settings".rs,
                        id = ACTION_NAVIGATE_SETTINGS
                    )
                ),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText(
                "Navigate to Settings",
                fontSize = 16.sp.asRemoteTextUnit(),
                color = RemoteColor(Color.White),
            )
        }
    }
}
