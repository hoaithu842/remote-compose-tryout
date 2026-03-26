package io.github.hoaithu842.rachel.remotecompose.controller.composables

import android.annotation.SuppressLint
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCollapsibleColumn
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.asRemoteTextUnit
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val CollapsibleSpec = RemoteSpec(
    fileName = "composable_collapsible.rc",
    content = { ComponentCollapsible() },
)

@SuppressLint("RestrictedApiAndroidX", "RestrictedApi")
@Preview
@Composable
fun ComponentCollapsible() {
    RemoteColumn(
        modifier = RemoteModifier.fillMaxSize().padding(16.dp),
        verticalArrangement = RemoteArrangement.SpaceEvenly,
    ) {
        RemoteText(
            text = "RemoteCollapsibleColumn",
            fontSize = 18.sp.asRemoteTextUnit(),
            color = RemoteColor(Color(0xFF6200EE)),
        )
        RemoteText(
            "High-priority items stay visible; low-priority items collapse when space is tight.",
            color = RemoteColor(Color.DarkGray),
        )

        RemoteCollapsibleColumn(
            modifier = RemoteModifier
                .fillMaxSize()
                .background(Color(0xFFEEEEEE))
                .padding(8.dp),
            verticalArrangement = RemoteArrangement.SpaceEvenly,
            horizontalAlignment = RemoteAlignment.Start,
        ) {
            // Priority 1 – always visible
            RemoteBox(
                modifier = RemoteModifier
                    .background(Color(0xFF6200EE))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .priority(1f),
            ) {
                RemoteText("Priority 1 – always shown", color = RemoteColor(Color.White))
            }

            // Priority 2 – shown when space allows
            RemoteBox(
                modifier = RemoteModifier
                    .background(Color(0xFF03DAC6))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .priority(2f),
            ) {
                RemoteText("Priority 2 – secondary", color = RemoteColor(Color.Black))
            }

            // Priority 3 – first to collapse
            RemoteBox(
                modifier = RemoteModifier
                    .background(Color(0xFFFFEB3B))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .priority(3f),
            ) {
                RemoteText("Priority 3 – collapses first", color = RemoteColor(Color.Black))
            }

            // Priority 4 – collapses before priority 3
            RemoteBox(
                modifier = RemoteModifier
                    .background(Color(0xFFFF5722))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .priority(4f),
            ) {
                RemoteText("Priority 4 – collapses earliest", color = RemoteColor(Color.White))
            }
        }
    }
}
