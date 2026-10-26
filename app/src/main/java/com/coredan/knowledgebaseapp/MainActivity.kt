package com.coredan.knowledgebaseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.coredan.knowledgebaseapp.ui.placeholder.PlaceholderRoute
import com.coredan.knowledgebaseapp.ui.theme.KnowledgeBaseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KnowledgeBaseTheme {
                PlaceholderRoute()
            }
        }
    }
}
