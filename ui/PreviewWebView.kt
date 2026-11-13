package com.example.codeeditor3d.ui

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.codeeditor3d.logic.CodeError
import com.example.codeeditor3d.logic.Severity
import com.example.codeeditor3d.model.Language

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PreviewWebView(
    code: String,
    language: Language,
    modifier: Modifier = Modifier,
    onRuntimeError: (CodeError) -> Unit = {}
) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            WebView(ctx).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.allowFileAccess = true
                webViewClient = WebViewClient()
                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun reportError(message: String, line: Int) {
                        onRuntimeError(CodeError(line, "Runtime: $message", Severity.ERROR))
                    }
                }, "AndroidBridge")
            }
        },
        update = { wv ->
            wv.loadDataWithBaseURL(null, buildPreviewHtml(code, language), "text/html", "utf-8", null)
        }
    )
}

private fun buildPreviewHtml(code: String, language: Language): String {
    val errorCatcher = """
        <script>
          window.onerror = function(msg, src, line) {
            try { AndroidBridge.reportError(String(msg), line|0); } catch(e) {}
            var p = document.getElementById('__err');
            if (p) p.textContent += msg + ' (line ' + line + ')\n';
          };
        </script>
    """.trimIndent()

    return when (language.id) {
        "html" -> "<!DOCTYPE html><html><head><meta name='viewport' content='width=device-width,initial-scale=1'>$errorCatcher</head><body>$code<pre id='__err' style='color:#f66;font:12px monospace;white-space:pre-wrap'></pre></body></html>"
        "css"  -> "<!DOCTYPE html><html><head><meta name='viewport' content='width=device-width,initial-scale=1'><style>$code</style></head><body><h1>Preview</h1><div class='box'>Sample</div><p class='demo'>Hello CSS</p></body></html>"
        "js", "ts" -> "<!DOCTYPE html><html><head><meta name='viewport' content='width=device-width,initial-scale=1'>$errorCatcher</head><body><pre id='__log' style='color:#9ce37d;font:13px monospace;white-space:pre-wrap'></pre><pre id='__err' style='color:#f66;font:12px monospace;white-space:pre-wrap'></pre><script>var _l=console.log;console.log=function(){_l.apply(console,arguments);var p=document.getElementById('__log');p.textContent+=[].slice.call(arguments).map(String).join(' ')+'\\n';};try{$code}catch(e){document.getElementById('__err').textContent+=e+'\\n';try{AndroidBridge.reportError(String(e),0);}catch(_){}}</script></body></html>"
        "json" -> "<!DOCTYPE html><html><head><meta name='viewport' content='width=device-width,initial-scale=1'></head><body><pre id='out' style='color:#9ce37d;font:13px monospace;white-space:pre-wrap'></pre><script>try{JSON.parse(${jsQuote(code)});document.getElementById('out').textContent='✓ JSON صالح';}catch(e){document.getElementById('out').textContent='✗ '+e.message;try{AndroidBridge.reportError(String(e.message),0);}catch(_){}}</script></body></html>"
        "py" -> """
            <!DOCTYPE html><html><head><meta name='viewport' content='width=device-width,initial-scale=1'></head><body>
            <pre id='out' style='color:#e6e9f0;font:12px monospace;white-space:pre-wrap'>تحميل محرك Python (Pyodide)... يتطلب اتصالاً بالإنترنت</pre>
            <script src='https://cdn.jsdelivr.net/pyodide/v0.26.2/full/pyodide.js'></script>
            <script>
              async function run(){
                try{
                  const py = await loadPyodide();
                  py.setStdout({batched:(s)=>document.getElementById('out').textContent+=s+"\n"});
                  py.setStderr({batched:(s)=>document.getElementById('out').textContent+="ERR: "+s+"\n"});
                  await py.runPythonAsync(${jsQuote(code)});
                }catch(e){document.getElementById('out').textContent+="✗ "+e;try{AndroidBridge.reportError(String(e),0);}catch(_){}}
              } run();
            </script></body></html>
        """.trimIndent()
        else -> "<!DOCTYPE html><html><body style='color:#8892a6;font:14px monospace;padding:16px'>" +
                "المعاينة الحية متاحة لـ HTML/CSS/JS/TS/JSON/Python.<br>للغة ${language.display} يعمل المحلل الساكن فقط." +
                "</body></html>"
    }
}

private fun jsQuote(s: String): String =
    "\"" + s.replace("\\", "\\\\").replace("\"", "\\"").replace("\n", "\\n").replace("\r", "") + "\""
