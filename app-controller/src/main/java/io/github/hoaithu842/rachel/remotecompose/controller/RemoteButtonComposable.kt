package io.github.hoaithu842.rachel.remotecompose.controller

import android.content.Context
import androidx.compose.remote.creation.compose.capture.captureSingleRemoteDocument
import io.github.hoaithu842.rachel.remotecompose.controller.composables.AnimatedVisibilitySpec
import io.github.hoaithu842.rachel.remotecompose.controller.composables.ButtonSpec
import io.github.hoaithu842.rachel.remotecompose.controller.composables.CollapsibleSpec
import io.github.hoaithu842.rachel.remotecompose.controller.composables.GradientsSpec
import io.github.hoaithu842.rachel.remotecompose.controller.composables.LayoutsSpec
import io.github.hoaithu842.rachel.remotecompose.controller.composables.MarqueeSpec
import io.github.hoaithu842.rachel.remotecompose.controller.composables.ModifiersSpec
import io.github.hoaithu842.rachel.remotecompose.controller.composables.RemoteSpec
import io.github.hoaithu842.rachel.remotecompose.controller.composables.ScrollSpec
import io.github.hoaithu842.rachel.remotecompose.controller.composables.StateLayoutSpec
import io.github.hoaithu842.rachel.remotecompose.controller.composables.TypographySpec

// ─── Active spec — swap this to change what gets generated ───────────────────
val activeSpec = CollapsibleSpec

// ButtonSpec
// TypographySpec
// LayoutsSpec
// GradientsSpec
// ModifiersSpec
// AnimatedVisibilitySpec
// StateLayoutSpec
// ScrollSpec
// MarqueeSpec
// CollapsibleSpec

suspend fun generateDocument(context: Context, spec: RemoteSpec = activeSpec): ByteArray {
    return captureSingleRemoteDocument(
        context = context,
        content = spec.content,
    ).bytes
}
