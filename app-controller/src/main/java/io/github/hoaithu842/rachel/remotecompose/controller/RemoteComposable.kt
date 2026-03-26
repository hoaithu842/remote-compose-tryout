package io.github.hoaithu842.rachel.remotecompose.controller

import android.content.Context
import androidx.compose.remote.creation.compose.capture.captureSingleRemoteDocument
import io.github.hoaithu842.rachel.remotecompose.controller.RemoteSpec
import io.github.hoaithu842.rachel.remotecompose.controller.composables.UseCaseNavigationSpec

// ─── Active spec — swap this to change what gets generated ───────────────────
val activeSpec = UseCaseNavigationSpec

// ── Component demos ───────────────────────────────────────────────────────────
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
// ImagesSpec

// ── Use cases ─────────────────────────────────────────────────────────────────
// UseCaseNavigationSpec  ← navigation home (Profile + Settings buttons)
// UseCaseProfileSpec     ← served at /profile
// UseCaseSettingsSpec    ← served at /settings

suspend fun generateDocument(context: Context, spec: RemoteSpec = activeSpec): ByteArray {
    return captureSingleRemoteDocument(
        context = context,
        content = spec.content,
    ).bytes
}
