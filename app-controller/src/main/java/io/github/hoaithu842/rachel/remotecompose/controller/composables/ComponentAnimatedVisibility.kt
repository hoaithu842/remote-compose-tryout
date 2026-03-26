package io.github.hoaithu842.rachel.remotecompose.controller.composables

import android.annotation.SuppressLint
import androidx.compose.remote.core.operations.layout.animation.AnimationSpec.ANIMATION
import androidx.compose.remote.core.operations.utilities.easing.GeneralEasing
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.layout.StateLayout
import androidx.compose.remote.creation.compose.layout.rememberStateMachine
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.animationSpec
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.clickable
import androidx.compose.remote.creation.compose.modifier.clip
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.modifier.visibility
import androidx.compose.remote.creation.compose.shapes.RemoteRoundedCornerShape
import androidx.compose.remote.creation.compose.state.MutableRemoteState
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.RemoteDp
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hoaithu842.rachel.remotecompose.controller.RemoteSpec

val AnimatedVisibilitySpec = RemoteSpec(
    fileName = "composable_animated_visibility.rc",
    content = { RemoteAnimatedVisibilityComposable() },
)

private const val STATE_HIDDEN = 0
private const val STATE_VISIBLE = 1

@SuppressLint("RestrictedApiAndroidX", "RestrictedApi")
@Preview
@Composable
fun RemoteAnimatedVisibilityComposable() {
    // State machine: 0 = hidden, 1 = visible
    val machine = rememberStateMachine(STATE_HIDDEN, STATE_VISIBLE)
    val mutableState = machine.currentState as MutableRemoteState<Int>

    RemoteColumn(
        modifier = RemoteModifier.fillMaxSize().padding(16.dp),
//        verticalArrangement = RemoteArrangement.SpaceEvenly,
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
    ) {
        RemoteText(
            "Tap the button to toggle animated visibility",
            color = RemoteColor(Color.DarkGray),
        )

        // ── Toggle button ────────────────────────────────────────
        RemoteBox(
            modifier = RemoteModifier
                .clip(RemoteRoundedCornerShape(RemoteDp(8.dp)))
                .background(Color(0xFF6200EE))
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .clickable(
                    androidx.compose.remote.creation.compose.action.ValueChange(
                        mutableState,
                        // cycle: hidden → visible → hidden
                        androidx.compose.remote.creation.compose.state.selectIfLt(
                            machine.currentState,
                            STATE_VISIBLE.ri,
                            STATE_VISIBLE.ri,
                            STATE_HIDDEN.ri,
                        ),
                    )
                ),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText("Toggle", color = RemoteColor(Color.White))
        }

        // ── Content with animated visibility ─────────────────────
        StateLayout(machine) { state ->
            RemoteBox(
                modifier = RemoteModifier
                    .size(RemoteDp(200.dp), RemoteDp(100.dp))
                    .clip(RemoteRoundedCornerShape(RemoteDp(12.dp)))
                    .background(Color(0xFF03DAC6))
                    .visibility(state.ri)
                    .animationSpec(
                        motionDuration = 400f,
                        motionEasingType = GeneralEasing.CUBIC_STANDARD,
                        visibilityDuration = 400f,
                        visibilityEasingType = GeneralEasing.CUBIC_STANDARD,
                        enterAnimation = ANIMATION.FADE_IN,
                        exitAnimation = ANIMATION.FADE_OUT,
                    ),
                horizontalAlignment = RemoteAlignment.CenterHorizontally,
                verticalArrangement = RemoteArrangement.Center,
            ) {
                RemoteText("I fade in and out!", color = RemoteColor(Color.Black))
            }
        }
    }
}
