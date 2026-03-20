package io.github.hoaithu842.rachel.remotecompose.tryout

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.remote.core.operations.Header
import androidx.compose.remote.core.operations.layout.managers.BoxLayout
import androidx.compose.remote.core.operations.layout.managers.ColumnLayout
import androidx.compose.remote.core.operations.layout.managers.CoreText
import androidx.compose.remote.creation.JvmRcPlatformServices
import androidx.compose.remote.creation.RemoteComposeWriter
import androidx.compose.remote.creation.actions.HostAction
import androidx.compose.remote.creation.modifiers.RecordingModifier
import androidx.compose.remote.creation.modifiers.RoundedRectShape
import java.io.File

/** Config used when generating the button document (e.g. color, label). */
data class ButtonDocumentConfig(
    val buttonColor: Int = 0xFF6200EA.toInt(),
    val buttonLabel: String = "Click Me",
)

@SuppressLint("RestrictedApi")
fun createButtonDocument(config: ButtonDocumentConfig = ButtonDocumentConfig()): ByteArray {
    val density = 2.625f
    fun dp(v: Int) = v * density

    val writer = RemoteComposeWriter(
        JvmRcPlatformServices(),
        RemoteComposeWriter.HTag(Header.DOC_WIDTH, (400 * density).toInt()),
        RemoteComposeWriter.HTag(Header.DOC_HEIGHT, (800 * density).toInt()),
    )

    val radius = dp(24)
    val shape = RoundedRectShape(radius, radius, radius, radius)

    val buttonMod = RecordingModifier()
        .fillMaxWidth()
        .clip(shape)
        .background(config.buttonColor)
        .onClick(HostAction(1001, writer.addText("my_action")))
        .padding(dp(32), dp(14), dp(32), dp(14))

    writer.root {
        writer.column(
            RecordingModifier().fillMaxSize().padding(dp(24)),
            ColumnLayout.CENTER,
            ColumnLayout.CENTER
        ) {
            writer.startBox(buttonMod, BoxLayout.CENTER, BoxLayout.CENTER)
            writer.textComponent(
                RecordingModifier(),
                writer.addText(config.buttonLabel),
                0xFFFFFFFF.toInt(),
                dp(16),
                0,
                600f,
                null,
                CoreText.TEXT_ALIGN_CENTER,
                0,
                Int.MAX_VALUE
            ) {}
            writer.endBox()
        }
    }

    return writer.encodeToByteArray()
}

/** Directory name under app files dir where .rc files are stored. */
private const val RC_DIR = "documents"

/** Default filename for the saved .rc document. */
const val DEFAULT_RC_FILENAME = "saved.rc"

/**
 * Saves a Remote Compose document byte array to a local .rc file in app storage.
 * Path: context.filesDir/documents/[filename]
 * @return true if saved successfully, false otherwise.
 */
fun saveDocumentToFile(
    context: Context,
    bytes: ByteArray,
    filename: String = DEFAULT_RC_FILENAME
): Boolean {
    return try {
        val dir = File(context.filesDir, RC_DIR).apply { mkdirs() }
        val file = File(dir, filename)
        file.writeBytes(bytes)
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Loads a .rc document from app storage.
 * Path: context.filesDir/documents/[filename]
 * @return the byte array, or null if file does not exist or read failed.
 */
fun loadDocumentFromFile(context: Context, filename: String = DEFAULT_RC_FILENAME): ByteArray? {
    return try {
        val file = File(context.filesDir, RC_DIR).resolve(filename)
        if (file.isFile) file.readBytes() else null
    } catch (e: Exception) {
        null
    }
}