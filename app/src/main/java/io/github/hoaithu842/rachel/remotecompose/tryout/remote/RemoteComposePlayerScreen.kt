@file:SuppressLint("RestrictedApiAndroidX")

package io.github.hoaithu842.rachel.remotecompose.tryout.remote

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.remote.player.view.RemoteComposePlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val DEFAULT_DOCUMENT_URL = "http://10.0.2.2:8081/remote/document"

@Composable
fun RemoteComposePlayerScreen(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
) {
    val scope = rememberCoroutineScope()

    var documentUrl by remember { mutableStateOf(DEFAULT_DOCUMENT_URL) }
    var documentBytes by remember { mutableStateOf<ByteArray?>(null) }
    var loadState by remember { mutableStateOf<LoadState>(LoadState.Idle) }

    fun loadDocument() {
        scope.launch {
            loadState = LoadState.Loading
            loadState = withContext(Dispatchers.IO) {
                RemoteDocumentLoader.loadFromUrl(documentUrl)
                    .fold(
                        onSuccess = { bytes ->
                            documentBytes = bytes
                            LoadState.Success
                        },
                        onFailure = { LoadState.Error(it.message ?: "Unknown error") }
                    )
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (onBack != null) {
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("← Back to color")
            }
        }
        Text(
            text = "Server-driven UI",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        OutlinedTextField(
            value = documentUrl,
            onValueChange = { documentUrl = it },
            label = { Text("Remote document URL") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Button(
            onClick = { loadDocument() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Load / Refresh")
        }

        when (val state = loadState) {
            is LoadState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.Error -> {
                Text(
                    text = "Error: ${state.message}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            else -> {}
        }

        Spacer(modifier = Modifier.height(8.dp))

        when {
            documentBytes != null -> {
                RemoteDocumentView(
                    documentBytes = documentBytes!!,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp),
                    contentKey = documentBytes!!.contentHashCode(),
                    onAction = { _, _ -> /* Host actions from .rc (e.g. navigation) */ },
                )
            }

            loadState is LoadState.Idle -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Enter Remote document URL and tap Load.\n\nEmulator: http://10.0.2.2:8081/remote/document",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

/**
 * Renders a Remote Compose document. Matches the approach from
 * [armcha/remotecompose-android RemoteDocumentView](https://github.com/armcha/remotecompose-android/blob/main/app/src/main/java/com/example/remotecompose/ui/components/RemoteDocumentView.kt):
 * direct use of [RemoteComposePlayer] with [SuppressLint("RestrictedApiAndroidX")], [key] for
 * recomposition when document changes, and [addIdActionListener] for host actions.
 */
@Composable
fun RemoteDocumentView(
    documentBytes: ByteArray,
    modifier: Modifier = Modifier,
    contentKey: Any = documentBytes.contentHashCode(),
    onAction: (id: Int, metadata: String?) -> Unit = { _, _ -> },
) {
    key(contentKey) {
        AndroidView(
            factory = { ctx ->
                RemoteComposePlayer(ctx).apply {
                    setDocument(documentBytes)
                    addIdActionListener { id, metadata ->
                        onAction(id, metadata)
                    }
                }
            },
            modifier = modifier,
        )
    }
}

private sealed class LoadState {
    data object Idle : LoadState()
    data object Loading : LoadState()
    data object Success : LoadState()
    data class Error(val message: String) : LoadState()
}
