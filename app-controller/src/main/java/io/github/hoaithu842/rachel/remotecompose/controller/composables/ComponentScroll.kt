package io.github.hoaithu842.rachel.remotecompose.controller.composables

import android.annotation.SuppressLint
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.rememberRemoteScrollState
import androidx.compose.remote.creation.compose.modifier.verticalScroll
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.asRemoteTextUnit
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.hoaithu842.rachel.remotecompose.controller.RemoteSpec

val ScrollSpec = RemoteSpec(
    fileName = "composable_scroll.rc",
    content = { ComponentScroll() },
)

@SuppressLint("RestrictedApiAndroidX", "RestrictedApi")
@Preview
@Composable
fun ComponentScroll() {
    val scrollState = rememberRemoteScrollState()

    RemoteColumn(
        modifier = RemoteModifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = RemoteArrangement.SpaceEvenly,
    ) {
        RemoteText(
            "Vertical Scroll — 20 items",
            fontSize = 18.sp.asRemoteTextUnit(),
            color = RemoteColor(Color(0xFF6200EE)),
        )

        repeat(20) { index ->
            RemoteBox(
                modifier = RemoteModifier
                    .background(
                        if (index % 2 == 0) Color(0xFFEDE7F6) else Color(0xFFE8EAF6)
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp),
            ) {
                RemoteText(
                    "Item #${index + 1}",
                    color = RemoteColor(Color(0xFF37474F)),
                )
            }
        }
    }
}
