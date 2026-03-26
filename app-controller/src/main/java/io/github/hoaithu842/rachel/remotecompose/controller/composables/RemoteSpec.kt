package io.github.hoaithu842.rachel.remotecompose.controller.composables

import androidx.compose.runtime.Composable

data class RemoteSpec(
    val fileName: String,
    val content: @Composable () -> Unit,
)