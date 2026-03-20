package io.github.hoaithu842.rachel.remotecompose.tryout

import android.content.Context
import android.util.Log
import androidx.compose.remote.creation.compose.capture.captureSingleRemoteDocument
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Yellow

@Composable
fun Greeting() {
    RemoteBox() {
        RemoteText(text = "Hehehe", color = RemoteColor(color = Yellow))
    }
}

suspend fun temp(context: Context): ByteArray {
    val bin = captureSingleRemoteDocument(
        context = context,
        content = { Greeting() },
    )
    Log.d("rachel", "temp: ${bin.bytes}")
    return bin.bytes
}