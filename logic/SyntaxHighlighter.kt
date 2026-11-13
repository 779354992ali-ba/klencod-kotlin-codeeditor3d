package com.example.codeeditor3d.logic

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.example.codeeditor3d.model.Language

object SyntaxHighlighter {

    private val COL_KEYWORD = Color(0xFFC586F0)
    private val COL_BUILTIN = Color(0xFF6BB6FF)
    private val COL_STRING  = Color(0xFF9CE37D)
    private val COL_NUMBER  = Color(0xFFF7B955)
    private val COL_COMMENT = Color(0xFF7A8296)
    private val COL_TAG     = Color(0xFFFF7B72)
    private val COL_PROP    = Color(0xFF8BD5FF)

    fun highlight(code: String, lang: Language): AnnotatedString = buildAnnotatedString {
        append(code)
        if (code.isEmpty()) return@buildAnnotatedString

        var i = 0
        val n = code.length
        while (i < n) {
            val c = code[i]
            val next = if (i + 1 < n) code[i + 1] else '\u0000'

            if (c == '/' && next == '/') {
                val end = code.indexOf('\n', i).let { if (it == -1) n else it }
                addStyle(SpanStyle(color = COL_COMMENT, fontStyle = FontStyle.Italic), i, end)
                i = end; continue
            }
            if (c == '/' && next == '*') {
                val end = code.indexOf("*/", i + 2).let { if (it == -1) n else it + 2 }
                addStyle(SpanStyle(color = COL_COMMENT, fontStyle = FontStyle.Italic), i, end)
                i = end; continue
            }
            if (lang.id == "py" && c == '#') {
                val end = code.indexOf('\n', i).let { if (it == -1) n else it }
                addStyle(SpanStyle(color = COL_COMMENT, fontStyle = FontStyle.Italic), i, end)
                i = end; continue
            }
            if (lang.id == "html" && c == '<' && next == '!' && i + 3 < n && code.startsWith("<!--", i)) {
                val end = code.indexOf("-->", i).let { if (it == -1) n else it + 3 }
                addStyle(SpanStyle(color = COL_COMMENT, fontStyle = FontStyle.Italic), i, end)
                i = end; continue
            }

            if (c == '"' || c == ''' || c == '`') {
                val quote = c
                var j = i + 1
                while (j < n) {
                    if (code[j] == '\\') { j += 2; continue }
                    if (code[j] == quote || code[j] == '\n') { j++; break }
                    j++
                }
                addStyle(SpanStyle(color = COL_STRING), i, j.coerceAtMost(n))
                i = j; continue
            }

            if (c.isDigit()) {
                var j = i
                while (j < n && (code[j].isLetterOrDigit() || code[j] == '.' || code[j] == '_')) j++
                addStyle(SpanStyle(color = COL_NUMBER), i, j)
                i = j; continue
            }

            if (c.isLetter() || c == '_' || c == '$') {
                var j = i
                while (j < n && (code[j].isLetterOrDigit() || code[j] == '_' || code[j] == '$')) j++
                val word = code.substring(i, j)
                when {
                    word in lang.keywords -> addStyle(SpanStyle(color = COL_KEYWORD, fontWeight = FontWeight.Bold), i, j)
                    word in lang.builtins -> addStyle(SpanStyle(color = COL_BUILTIN), i, j)
                    lang.id == "html" && word in lang.keywords -> addStyle(SpanStyle(color = COL_TAG), i, j)
                    lang.id == "css" && word in lang.keywords -> addStyle(SpanStyle(color = COL_PROP), i, j)
                }
                i = j; continue
            }
            i++
        }
    }
}

class HighlightTransformation(private val lang: Language) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(SyntaxHighlighter.highlight(text.text, lang), OffsetMapping.Identity)
    }
}
