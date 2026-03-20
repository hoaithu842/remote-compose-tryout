package io.github.hoaithu842.rachel.remotecompose.tryout.remote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.hoaithu842.rachel.remotecompose.tryout.temp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// From the emulator, 10.0.2.2 is the host (your laptop). Port 8081 to avoid conflict with Jenkins on 8080.

@Composable
fun RemoteColorScreen(
    modifier: Modifier = Modifier,
    onOpenDocument: (ByteArray) -> Unit,
) {
    var error by remember { mutableStateOf<String?>(null) }
    var helloText by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF6200EE)),
    ) {
        helloText?.let { text ->
            Text(
                text = text,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
            )
        }
        error?.let { msg ->
            Text(
                text = msg,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
            )
        }
        FloatingActionButton(
            onClick = {
                error = null
                scope.launch {
                    val bytes = withContext(Dispatchers.IO) {
                        temp(context)
                    }
                    onOpenDocument(bytes)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            Text("Doc")
        }
    }
}
