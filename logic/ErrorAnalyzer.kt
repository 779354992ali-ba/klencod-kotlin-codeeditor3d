package com.example.codeeditor3d.logic

import com.example.codeeditor3d.model.Language
import org.json.JSONException
import org.json.JSONObject

enum class Severity { ERROR, WARNING, INFO }

data class CodeError(val line: Int, val message: String, val severity: Severity)

object ErrorAnalyzer {

    fun analyze(code: String, lang: Language): List<CodeError> {
        val errors = mutableListOf<CodeError>()
        bracketCheck(code, errors)
        when (lang.id) {
            "json" -> checkJson(code, errors)
            "html" -> checkHtml(code, errors)
            "py"  -> checkPython(code, errors)
            "js", "ts" -> checkJs(code, errors)
        }
        return errors.sortedBy { it.line }
    }

    private fun bracketCheck(code: String, out: MutableList<CodeError>) {
        val stack = ArrayDeque<Pair<Char, Int>>()
        var line = 1
        var i = 0
        var inString: Char? = null
        var inLineComment = false
        var inBlockComment = false
        val n = code.length
        while (i < n) {
            val c = code[i]
            val next = if (i + 1 < n) code[i + 1] else '\u0000'
            if (c == '\n') { line++; inLineComment = false; i++; continue }
            if (inLineComment) { i++; continue }
            if (inBlockComment) {
                if (c == '*' && next == '/') { inBlockComment = false; i += 2 } else i++
                continue
            }
            if (inString != null) {
                if (c == '\\') { i += 2; continue }
                if (c == inString) inString = null
                i++; continue
            }
            when {
                c == '/' && next == '/' -> { inLineComment = true; i += 2 }
                c == '/' && next == '*' -> { inBlockComment = true; i += 2 }
                c == '"' || c == ''' || c == '`' -> { inString = c; i++ }
                c == '(' || c == '{' || c == '[' -> { stack.addLast(c to line); i++ }
                c == ')' || c == '}' || c == ']' -> {
                    if (stack.isEmpty()) {
                        out += CodeError(line, "قوس إغلاق غير متوقع: $c", Severity.ERROR); i++
                    } else {
                        val (open, _) = stack.removeLast()
                        if (!matches(open, c))
                            out += CodeError(line, "قوس غير متطابق: فُتح '$open' وأُغلق '$c'", Severity.ERROR)
                        i++
                    }
                }
                else -> i++
            }
        }
        if (inString != null) out += CodeError(line, "نص غير مغلق (${inString})", Severity.ERROR)
        if (inBlockComment) out += CodeError(line, "تعليق كتلي غير مغلق", Severity.ERROR)
        stack.forEach { (c, l) -> out += CodeError(l, "قوس غير مغلق: $c", Severity.ERROR) }
    }

    private fun matches(o: Char, c: Char) =
        (o == '(' && c == ')') || (o == '{' && c == '}') || (o == '[' && c == ']')

    private fun checkJson(code: String, out: MutableList<CodeError>) {
        if (code.isBlank()) return
        try {
            JSONObject(code)
        } catch (e: JSONException) {
            val msg = e.message ?: "JSON غير صالح"
            val line = Regex("line (\\d+)").find(msg)?.groupValues?.get(1)?.toIntOrNull() ?: 1
            out += CodeError(line, "JSON: $msg", Severity.ERROR)
        }
    }

    private fun checkHtml(code: String, out: MutableList<CodeError>) {
        val unclosed = mutableListOf<String>()
        val regex = Regex("<(/?)([a-zA-Z][a-zA-Z0-9]*)[^>]*?(/?)>")
        val voidTags = setOf("br","hr","img","input","meta","link","area","base","col","embed","source","track","wbr")
        for (m in regex.findAll(code)) {
            val closing = m.groupValues[1] == "/"
            val tag = m.groupValues[2].lowercase()
            val selfClose = m.groupValues[3] == "/"
            if (voidTags.contains(tag) || selfClose) continue
            if (!closing) unclosed.add(tag)
            else if (unclosed.isNotEmpty() && unclosed.last() == tag) unclosed.removeAt(unclosed.size - 1)
            else out += CodeError(lineOf(code, m.range.first), "وسم إغلاق غير متوقع: </$tag>", Severity.WARNING)
        }
        unclosed.forEach { out += CodeError(1, "وسم غير مغلق: <$it>", Severity.WARNING) }
    }

    private fun checkPython(code: String, out: MutableList<CodeError>) {
        code.lines().forEachIndexed { idx, l ->
            val t = l.trim()
            if (t.startsWith("def ") && !t.contains(":"))
                out += CodeError(idx + 1, "تعريف دالة بدون ':'", Severity.WARNING)
            if (t.startsWith("class ") && !t.contains(":"))
                out += CodeError(idx + 1, "تعريف كلاس بدون ':'", Severity.WARNING)
            if (t.endsWith(":") && idx == code.lines().lastIndex)
                out += CodeError(idx + 1, "كتلة بدون جسم بعد ':'", Severity.WARNING)
        }
    }

    private fun checkJs(code: String, out: MutableList<CodeError>) {
        val badAssign = Regex("\\b(if|while|for)\\s*\\([^)]*=[^=]")
        badAssign.findAll(code).forEach {
            out += CodeError(lineOf(code, it.range.first), "استخدام '=' داخل شرط (ربما تقصد '===')", Severity.WARNING)
        }
    }

    private fun lineOf(code: String, index: Int): Int =
        code.substring(0, index.coerceAtMost(code.length)).count { it == '\\n' } + 1
}
