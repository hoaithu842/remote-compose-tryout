package io.github.hoaithu842.rachel.remotecompose.controller

import androidx.compose.runtime.Composable

data class RemoteSpec(
    val fileName: String,
    val content: @Composable () -> Unit,
)