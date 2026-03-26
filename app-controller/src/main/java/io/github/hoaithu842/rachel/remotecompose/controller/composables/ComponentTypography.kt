package io.github.hoaithu842.rachel.remotecompose.controller.composables

import android.annotation.SuppressLint
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.asRemoteTextUnit
import androidx.compose.remote.creation.compose.text.RemoteTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val TypographySpec = RemoteSpec(
    fileName = "composable_typography.rc",
    content = { ComponentTypography() },
)

@SuppressLint("RestrictedApiAndroidX", "RestrictedApi")
@Preview
@Composable
fun ComponentTypography() {
    RemoteColumn(
        modifier = RemoteModifier.fillMaxSize().padding(16.dp),
        verticalArrangement = RemoteArrangement.SpaceEvenly,
    ) {
        RemoteText(
            "Display – 28sp Bold",
            fontSize = 28.sp.asRemoteTextUnit(),
            fontWeight = FontWeight.Bold
        )
        RemoteText("Headline – 22sp", fontSize = 22.sp.asRemoteTextUnit())
        RemoteText(
            "Title – 18sp Medium",
            fontSize = 18.sp.asRemoteTextUnit(),
            fontWeight = FontWeight.Medium
        )
        RemoteText("Body – 14sp Normal", fontSize = 14.sp.asRemoteTextUnit())
        RemoteText(
            "Caption – 12sp",
            fontSize = 12.sp.asRemoteTextUnit(),
            color = RemoteColor(Color.Gray)
        )

        RemoteText(
            "Italic text style",
            fontSize = 14.sp.asRemoteTextUnit(),
            fontStyle = FontStyle.Italic,
        )
        RemoteText(
            "Colored – Material Purple",
            fontSize = 14.sp.asRemoteTextUnit(),
            color = RemoteColor(Color(0xFF6200EE)),
        )
        RemoteText(
            "Underline decoration",
            fontSize = 14.sp.asRemoteTextUnit(),
            style = RemoteTextStyle(textDecoration = TextDecoration.Underline),
        )
        RemoteText(
            "Strikethrough decoration",
            fontSize = 14.sp.asRemoteTextUnit(),
            style = RemoteTextStyle(textDecoration = TextDecoration.LineThrough),
        )
        RemoteText(
            "Center aligned text that spans\nmultiple lines",
            fontSize = 14.sp.asRemoteTextUnit(),
            textAlign = TextAlign.Center,
        )
        RemoteText(
            text = "Overflow ellipsis on a very long text that should not wrap to the next line",
            fontSize = 14.sp.asRemoteTextUnit(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
