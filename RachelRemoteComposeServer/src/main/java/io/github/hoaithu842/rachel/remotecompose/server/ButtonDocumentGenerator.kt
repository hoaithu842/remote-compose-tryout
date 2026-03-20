package io.github.hoaithu842.rachel.remotecompose.server

import androidx.compose.remote.core.operations.layout.managers.BoxLayout
import androidx.compose.remote.core.operations.layout.managers.ColumnLayout
import androidx.compose.remote.core.operations.layout.managers.CoreText
import androidx.compose.remote.creation.JvmRcPlatformServices
import androidx.compose.remote.creation.RemoteComposeWriter
import androidx.compose.remote.creation.actions.HostAction
import androidx.compose.remote.creation.modifiers.RecordingModifier
import androidx.compose.remote.creation.modifiers.RoundedRectShape

/** Document size (logical pixels) for button .rc; matches app density. */
private const val DOC_WIDTH = 1050
private const val DOC_HEIGHT = 2100

/**
 * Builds a Remote Compose button document (.rc) with configurable color and label.
 * Used when the web localhost UI chooses the button color and saves; the byte array
 * is exposed at GET /remote/button.
 */
object ButtonDocumentGenerator {

    private val platform = JvmRcPlatformServices()

    /**
     * @param buttonColorHex hex color e.g. "#6200EE"
     * @param buttonLabel label text on the button
     */
    fun buildButtonDocument(
        buttonColorHex: String = "#6200EE",
        buttonLabel: String = "Click Me"
    ): ByteArray {
        val density = 2.625f
        fun dp(v: Int) = (v * density).toInt()

        val writer = RemoteComposeWriter(DOC_WIDTH, DOC_HEIGHT, "POC", platform)

        val buttonColorArgb = parseHexToArgb(buttonColorHex)
        val radius = dp(24).toFloat()
        val shape = RoundedRectShape(radius, radius, radius, radius)

        val buttonMod = RecordingModifier()
            .fillMaxWidth()
            .clip(shape)
            .background(buttonColorArgb)
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
                    writer.addText(buttonLabel),
                    0xFFFFFFFF.toInt(),
                    dp(16).toFloat(),
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

    private fun parseHexToArgb(hex: String): Int {
        val h = hex.removePrefix("#").take(6)
        if (h.length != 6) return 0xFF6200EE.toInt()
        return (0xFF shl 24) or (h.take(2).toInt(16) shl 16) or (h.substring(2, 4)
            .toInt(16) shl 8) or h.substring(4, 6).toInt(16)
    }
}
