package io.github.hoaithu842.rachel.remotecompose.controller.usecases

import android.annotation.SuppressLint
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
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

val UseCaseSettingsSpec =
    RemoteSpec(
        fileName = "settings.rc",
        content = { UseCaseSettings() },
    )

private val settingsItems = listOf(
    Triple("🔔", "Notifications",   "Manage push and email alerts"),
    Triple("🌙", "Dark Mode",        "Use dark theme across the app"),
    Triple("🔒", "Privacy",          "Data sharing and permissions"),
    Triple("🌐", "Language",         "English (United States)"),
    Triple("ℹ️", "About",            "Version 1.0.0"),
)

@SuppressLint("RestrictedApiAndroidX", "RestrictedApi")
@Preview
@Composable
fun UseCaseSettings() {
    RemoteColumn(
        modifier = RemoteModifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp),
    ) {
        RemoteText(
            "Settings",
            fontSize = 26.sp.asRemoteTextUnit(),
            color = RemoteColor(Color(0xFF1A1A2E)),
        )
        RemoteText(
            "App preferences",
            fontSize = 13.sp.asRemoteTextUnit(),
            color = RemoteColor(Color.Gray),
        )

        settingsItems.forEach { (icon, title, subtitle) ->
            SettingsRow(icon = icon, title = title, subtitle = subtitle)
        }
    }
}

@SuppressLint("RestrictedApiAndroidX", "RestrictedApi")
@Composable
private fun SettingsRow(icon: String, title: String, subtitle: String) {
    RemoteBox(
        modifier = RemoteModifier
            .clip(RemoteRoundedCornerShape(RemoteDp(8.dp)))
            .background(Color.White)
            .padding(12.dp),
    ) {
        RemoteRow(
            verticalAlignment = RemoteAlignment.CenterVertically,
            horizontalArrangement = RemoteArrangement.Start,
        ) {
            // Icon badge
            RemoteBox(
                modifier = RemoteModifier
                    .size(RemoteDp(40.dp))
                    .clip(RemoteRoundedCornerShape(RemoteDp(8.dp)))
                    .background(Color(0xFFF0EAFF)),
                horizontalAlignment = RemoteAlignment.CenterHorizontally,
                verticalArrangement = RemoteArrangement.Center,
            ) {
                RemoteText(icon, fontSize = 20.sp.asRemoteTextUnit())
            }

            RemoteColumn {
                RemoteText(
                    title,
                    fontSize = 15.sp.asRemoteTextUnit(),
                    color = RemoteColor(Color(0xFF1A1A2E)),
                )
                RemoteText(
                    subtitle,
                    fontSize = 12.sp.asRemoteTextUnit(),
                    color = RemoteColor(Color.Gray),
                )
            }
        }
    }
}
