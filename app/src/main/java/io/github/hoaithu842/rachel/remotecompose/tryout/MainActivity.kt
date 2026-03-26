package io.github.hoaithu842.rachel.remotecompose.tryout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.hoaithu842.rachel.remotecompose.tryout.remote.RemoteDocumentLoader
import io.github.hoaithu842.rachel.remotecompose.tryout.remote.RemoteDocumentView
import io.github.hoaithu842.rachel.remotecompose.tryout.ui.theme.RachelRemoteComposeTryoutTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

private const val SERVER_BASE = "http://10.0.2.2:8081"

// Action IDs — must match app-controller constants
private const val ACTION_NAVIGATE_PROFILE = 4001
private const val ACTION_NAVIGATE_SETTINGS = 4002

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RachelRemoteComposeTryoutTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var documentBytes by remember { mutableStateOf<ByteArray?>(null) }
    // Non-null when we've navigated away; holds the document to return to on back
    var backDocumentBytes by remember { mutableStateOf<ByteArray?>(null) }
    var status by remember { mutableStateOf("Connecting…") }
    var refreshTrigger by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(refreshTrigger) {
        status = "Loading…"
        val result = withContext(Dispatchers.IO) {
            RemoteDocumentLoader.fetchDocument(SERVER_BASE)
        }
        result.onSuccess { bytes ->
            if (backDocumentBytes == null) {
                // At root: update the displayed document
                documentBytes = bytes
                status = "Live  ✓"
            } else {
                // Navigated away: silently refresh the home doc so back returns a fresh copy
                backDocumentBytes = bytes
                status = "Live  ✓"
            }
        }.onFailure {
            status = "Error: ${it.message}"
        }
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            while (isActive) {
                try {
                    listenSSE { refreshTrigger++ }
                } catch (_: Exception) {
                }
                delay(3_000)
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Back button — visible when navigated away from root
                if (backDocumentBytes != null) {
                    IconButton(
                        onClick = {
                            documentBytes = backDocumentBytes
                            backDocumentBytes = null
                            status = "Live  ✓"
                        },
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(Modifier.width(4.dp))
                }
                Text("Remote Compose Tryout", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = { refreshTrigger++ },
                    modifier = Modifier.size(32.dp),
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                }
            }
            Text(
                status,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))

            if (documentBytes != null) {
                RemoteDocumentView(
                    documentBytes = documentBytes!!,
                    modifier = Modifier.fillMaxSize(),
                    onAction = { id, metadata ->
                        when (id) {
                            ACTION_NAVIGATE_PROFILE, ACTION_NAVIGATE_SETTINGS -> {
                                val path =
                                    if (id == ACTION_NAVIGATE_PROFILE) "/profile" else "/settings"
                                scope.launch {
                                    status = "Loading…"
                                    val result = withContext(Dispatchers.IO) {
                                        RemoteDocumentLoader.loadFromUrl("$SERVER_BASE$path")
                                    }
                                    result.onSuccess { bytes ->
                                        backDocumentBytes = documentBytes
                                        documentBytes = bytes
                                        status =
                                            if (id == ACTION_NAVIGATE_PROFILE) "Profile" else "Settings"
                                    }.onFailure {
                                        status = "Error: ${it.message}"
                                    }
                                }
                            }

                            else -> status = "Action $id"
                        }
                    },
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("No document loaded", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

private fun listenSSE(onUpdate: () -> Unit) {
    val url = URL("$SERVER_BASE/remote/events")
    val conn = url.openConnection() as HttpURLConnection
    conn.setRequestProperty("Accept", "text/event-stream")
    conn.connectTimeout = 10_000
    conn.readTimeout = 0
    try {
        conn.connect()
        BufferedReader(InputStreamReader(conn.inputStream)).use { reader ->
            while (true) {
                val line = reader.readLine() ?: break
                if (line.startsWith("data:")) {
                    onUpdate()
                }
            }
        }
    } finally {
        conn.disconnect()
    }
}
