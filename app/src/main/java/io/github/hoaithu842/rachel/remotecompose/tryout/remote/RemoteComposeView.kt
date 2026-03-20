package io.github.hoaithu842.rachel.remotecompose.tryout.remote

import android.annotation.SuppressLint
import androidx.compose.remote.player.view.RemoteComposePlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("RestrictedApiAndroidX", "RestrictedApi")
@Composable
fun RemoteDocumentView(
    documentBytes: ByteArray,
    modifier: Modifier = Modifier,
    onAction: (id: Int, metadata: String?) -> Unit = { _, _ -> },
) {
    key(documentBytes.contentHashCode()) {
        AndroidView(
            factory = { ctx ->
                RemoteComposePlayer(ctx).apply {
                    setDocument(documentBytes)
                    addIdActionListener { id, metadata ->
                        onAction(id, metadata)
                    }
                }
            },
            modifier = modifier
        )
    }
}