package com.example.codeeditor3d

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.codeeditor3d.ui.CodeForgeTheme
import com.example.codeeditor3d.ui.EditorScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodeForgeTheme {
                EditorScreen()
            }
        }
    }
}
