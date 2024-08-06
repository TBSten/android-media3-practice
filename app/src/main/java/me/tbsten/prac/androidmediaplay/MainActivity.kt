package me.tbsten.prac.androidmediaplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.tbsten.prac.androidmediaplay.ui.theme.AndroidMediaPlayPracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidMediaPlayPracticeTheme {
                    PlayerScreen()
            }
        }
    }
}

@Composable
internal fun PlayerScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    }
}
