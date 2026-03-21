package io.github.hoaithu842.rachel.remotecompose.controller

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.remote.player.view.RemoteComposePlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

private const val RC_FILENAME = "composable_button.rc"

class ControllerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                ControllerScreen()
            }
        }
    }
}

@Composable
fun ControllerScreen() {
    var documentBytes by remember { mutableStateOf<ByteArray?>(null) }
    var status by remember { mutableStateOf("Tap Generate to create .rc") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        documentBytes = withContext(Dispatchers.IO) { generateDocument(context) }
        status = "Preview loaded"
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Text("RC Controller", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(4.dp))
            Text(status, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    scope.launch {
                        status = "Generating…"
                        val bytes = withContext(Dispatchers.IO) { generateDocument(context) }
                        documentBytes = bytes
                        val dest = saveRcFile(context, bytes)
                        status = if (dest != null) {
                            "Saved → ${dest.absolutePath}"
                        } else {
                            "Generated (save failed)"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Generate & Save .rc")
            }

            Spacer(Modifier.height(16.dp))

            if (documentBytes != null) {
                Text("Preview", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(4.dp))
                PlayerView(
                    documentBytes = documentBytes!!,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@SuppressLint("RestrictedApiAndroidX", "RestrictedApi")
@Composable
private fun PlayerView(documentBytes: ByteArray, modifier: Modifier = Modifier) {
    key(documentBytes.contentHashCode()) {
        AndroidView(
            factory = { ctx ->
                RemoteComposePlayer(ctx).apply { setDocument(documentBytes) }
            },
            modifier = modifier,
        )
    }
}

/**
 * Writes the .rc bytes into the app's external files directory so you can
 * `adb pull` it straight into the server's documents/ folder:
 *
 *   adb pull /sdcard/Android/data/io.github.hoaithu842.rachel.remotecompose.controller/files/composable_button.rc \
 *       RachelRemoteComposeServer/documents/
 */
private fun saveRcFile(context: android.content.Context, bytes: ByteArray): File? {
    return try {
        val dir = context.getExternalFilesDir(null) ?: context.filesDir
        val file = File(dir, RC_FILENAME)
        file.writeBytes(bytes)
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
