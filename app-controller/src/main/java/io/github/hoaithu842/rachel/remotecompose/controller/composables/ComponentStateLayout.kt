package io.github.hoaithu842.rachel.remotecompose.controller.composables

import android.annotation.SuppressLint
import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.layout.StateLayout
import androidx.compose.remote.creation.compose.layout.rememberStateMachine
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.clickable
import androidx.compose.remote.creation.compose.modifier.clip
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.shapes.RemoteRoundedCornerShape
import androidx.compose.remote.creation.compose.state.MutableRemoteState
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.RemoteDp
import androidx.compose.remote.creation.compose.state.asRemoteTextUnit
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.hoaithu842.rachel.remotecompose.controller.RemoteSpec

val StateLayoutSpec = RemoteSpec(
    fileName = "composable_state_layout.rc",
    content = { ComponentStateLayout() },
)

private val tabColors = listOf(
    Color(0xFF6200EE),
    Color(0xFF03DAC6),
    Color(0xFFE91E63),
)
private val tabLabels = listOf("Tab A", "Tab B", "Tab C")
private val tabContent = listOf(
    "Purple screen — state 0",
    "Teal screen — state 1",
    "Pink screen — state 2",
)

@SuppressLint("RestrictedApiAndroidX", "RestrictedApi")
@Preview
@Composable
fun ComponentStateLayout() {
    val machine = rememberStateMachine(0, 1, 2)
    val mutableState = machine.currentState as MutableRemoteState<Int>

    RemoteColumn(
        modifier = RemoteModifier.fillMaxSize(),
        verticalArrangement = RemoteArrangement.Top,
    ) {
        // ── Tab bar ───────────────────────────────────────────────
        RemoteRow(
            modifier = RemoteModifier.background(Color(0xFFEEEEEE)),
            horizontalArrangement = RemoteArrangement.SpaceEvenly,
        ) {
            tabLabels.forEachIndexed { index, label ->
                RemoteBox(
                    modifier = RemoteModifier
                        .size(RemoteDp(100.dp), RemoteDp(48.dp))
                        .clickable(ValueChange(mutableState, index.ri)),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center,
                ) {
                    RemoteText(
                        label,
                        color = RemoteColor(Color(0xFF6200EE)),
                        fontSize = 14.sp.asRemoteTextUnit()
                    )
                }
            }
        }

        // ── Content per state ─────────────────────────────────────
        StateLayout(
            stateMachine = machine,
            modifier = RemoteModifier.fillMaxSize(),
        ) { state ->
            RemoteBox(
                modifier = RemoteModifier
                    .fillMaxSize()
                    .background(tabColors[state]),
                horizontalAlignment = RemoteAlignment.CenterHorizontally,
                verticalArrangement = RemoteArrangement.Center,
            ) {
                RemoteColumn(
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.SpaceEvenly,
                ) {
                    RemoteText(
                        tabContent[state],
                        color = RemoteColor(Color.White),
                        fontSize = 18.sp.asRemoteTextUnit(),
                    )
                    RemoteBox(
                        modifier = RemoteModifier
                            .clip(RemoteRoundedCornerShape(RemoteDp(24.dp)))
                            .background(Color.White.copy(alpha = 0.25f))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable(
                                ValueChange(mutableState, ((state + 1) % 3).ri)
                            ),
                    ) {
                        RemoteText("Next →", color = RemoteColor(Color.White))
                    }
                }
            }
        }
    }
}
