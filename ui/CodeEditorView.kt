package com.example.codeeditor3d.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codeeditor3d.logic.HighlightTransformation
import com.example.codeeditor3d.model.Language

@Composable
fun CodeEditorView(
    code: String,
    onCodeChange: (String) -> Unit,
    language: Language,
    modifier: Modifier = Modifier
) {
    val vScroll = rememberScrollState()
    val hScroll = rememberScrollState()
    val lineCount = code.lines().size.coerceAtLeast(1)
    val lineHeight = 22.sp

    Row(
        modifier
            .fillMaxSize()
            .background(Color(0xFF0F121B))
            .verticalScroll(vScroll)
    ) {
        Column(
            Modifier
                .width(44.dp)
                .background(Color(0xFF0B0E15))
                .padding(vertical = 8.dp, horizontal = 6.dp)
        ) {
            repeat(lineCount) { i ->
                Text(
                    text = "${i + 1}",
                    color = Color(0xFF5B6478),
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = lineHeight
                )
            }
        }
        BasicTextField(
            value = code,
            onValueChange = onCodeChange,
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(hScroll)
                .padding(8.dp),
            textStyle = TextStyle(
                color = Color(0xFFE6E9F0),
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                lineHeight = lineHeight
            ),
            cursorBrush = SolidColor(Color(0xFF4F8BFF)),
            visualTransformation = HighlightTransformation(language)
        )
    }
}
