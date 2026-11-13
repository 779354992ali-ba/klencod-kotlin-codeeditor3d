package com.example.codeeditor3d.model

import androidx.compose.ui.graphics.Color

enum class Engine { WEB, PYTHON, PLAIN }

data class Language(
    val id: String,
    val display: String,
    val ext: String,
    val engine: Engine,
    val keywords: Set<String> = emptySet(),
    val builtins: Set<String> = emptySet(),
    val accent: Color = Color(0xFF4F8BFF)
)

object Languages {
    val JS = Language(
        id = "js", display = "JavaScript", ext = "js", engine = Engine.WEB,
        keywords = setOf("var","let","const","function","return","if","else","for","while","do","switch","case","break","continue","new","class","extends","super","this","typeof","instanceof","in","of","try","catch","finally","throw","async","await","yield","import","export","from","default","null","undefined","true","false","void","delete"),
        builtins = setOf("console","Math","JSON","Array","Object","String","Number","Boolean","Promise","Map","Set","Date","RegExp","document","window"),
        accent = Color(0xFFF7DF1E)
    )
    val TS = Language(
        id = "ts", display = "TypeScript", ext = "ts", engine = Engine.WEB,
        keywords = JS.keywords + setOf("interface","type","enum","implements","public","private","protected","readonly","namespace","declare","as","is","keyof","infer","satisfies"),
        builtins = JS.builtins,
        accent = Color(0xFF3178C6)
    )
    val HTML = Language("html","HTML","html",Engine.WEB,
        keywords = setOf("html","head","body","div","span","p","a","img","script","style","link","meta","title","section","header","footer","nav","main","ul","ol","li","table","tr","td","th","form","input","button","canvas","svg","video","audio","iframe"),
        accent = Color(0xFFE34F26))
    val CSS = Language("css","CSS","css",Engine.WEB,
        keywords = setOf("color","background","margin","padding","border","display","flex","grid","position","top","left","right","bottom","width","height","font","font-size","font-family","transform","transition","animation","opacity","z-index","box-shadow","border-radius"),
        accent = Color(0xFF264DE4))
    val JSON = Language("json","JSON","json",Engine.WEB,
        keywords = setOf("true","false","null"),
        accent = Color(0xFF9AA0A6))
    val PY = Language("py","Python","py",Engine.PYTHON,
        keywords = setOf("def","class","return","if","elif","else","for","while","break","continue","import","from","as","try","except","finally","raise","with","lambda","yield","global","nonlocal","pass","and","or","not","in","is","None","True","False","async","await"),
        builtins = setOf("print","len","range","int","str","float","list","dict","set","tuple","open","sum","min","max","abs","sorted","enumerate","zip"),
        accent = Color(0xFF3776AB))
    val JAVA = Language("java","Java","java",Engine.PLAIN,
        keywords = setOf("public","private","protected","class","interface","extends","implements","static","final","void","int","long","double","float","boolean","char","new","return","if","else","for","while","do","switch","case","break","continue","try","catch","finally","throw","throws","import","package","this","super","null","true","false","abstract","synchronized","volatile"),
        accent = Color(0xFFED8B00))
    val KT = Language("kt","Kotlin","kt",Engine.PLAIN,
        keywords = setOf("fun","val","var","class","interface","object","data","sealed","enum","when","if","else","for","while","do","return","break","continue","try","catch","finally","throw","import","package","this","super","null","true","false","is","as","in","out","by","lazy","suspend"),
        accent = Color(0xFF7F52FF))
    val C = Language("c","C","c",Engine.PLAIN,
        keywords = setOf("int","char","float","double","void","long","short","signed","unsigned","struct","union","enum","typedef","static","extern","const","return","if","else","for","while","do","switch","case","break","continue","goto","sizeof"),
        accent = Color(0xFF00599C))
    val CPP = Language("cpp","C++","cpp",Engine.PLAIN,
        keywords = C.keywords + setOf("class","public","private","protected","template","typename","namespace","using","new","delete","try","catch","throw","virtual","override","constexpr","auto","nullptr"),
        accent = Color(0xFF004482))
    val GO = Language("go","Go","go",Engine.PLAIN,
        keywords = setOf("func","var","const","package","import","type","struct","interface","map","chan","go","defer","select","case","switch","default","if","else","for","range","return","break","continue","nil","true","false"),
        accent = Color(0xFF00ADD8))
    val RS = Language("rs","Rust","rs",Engine.PLAIN,
        keywords = setOf("fn","let","mut","const","static","struct","enum","trait","impl","for","while","loop","if","else","match","return","break","continue","use","mod","pub","crate","super","self","as","in","where","async","await","move","ref","dyn"),
        accent = Color(0xFFDEA584))

    val all = listOf(JS, TS, HTML, CSS, JSON, PY, JAVA, KT, C, CPP, GO, RS)
}