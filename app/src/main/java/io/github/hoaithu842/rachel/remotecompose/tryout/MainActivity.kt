package io.github.hoaithu842.rachel.remotecompose.tryout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hoaithu842.rachel.remotecompose.tryout.remote.RemoteColorScreen
import io.github.hoaithu842.rachel.remotecompose.tryout.remote.RemoteDocumentLoader
import io.github.hoaithu842.rachel.remotecompose.tryout.remote.RemoteDocumentView
import io.github.hoaithu842.rachel.remotecompose.tryout.ui.theme.RachelRemoteComposeTryoutTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val SERVER_BASE = "http://10.0.2.2:8081"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RachelRemoteComposeTryoutTheme {
                var showDocumentScreen by remember { mutableStateOf(false) }
                var documentBytes by remember { mutableStateOf<ByteArray?>(null) }
                val scope = rememberCoroutineScope()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (showDocumentScreen && documentBytes != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            RemoteDocumentView(
                                documentBytes = documentBytes!!,
                                modifier = Modifier.fillMaxSize(),
                            )
                            FloatingActionButton(
                                onClick = { showDocumentScreen = false },
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp),
                            ) {
                                Text("← Back")
                            }
                            FloatingActionButton(
                                onClick = {
                                    scope.launch {
                                        val result = withContext(Dispatchers.IO) {
                                            RemoteDocumentLoader.fetchDocument(SERVER_BASE)
                                        }
                                        result.getOrNull()?.let { documentBytes = it }
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(16.dp),
                            ) {
                                Text("Refresh")
                            }
                        }
                    } else {
                        RemoteColorScreen(
                            modifier = Modifier.padding(innerPadding),
                            onOpenDocument = { bytes ->
                                documentBytes = bytes
                                showDocumentScreen = true
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RachelRemoteComposeTryoutTheme {
        Greeting("Android")
    }
}