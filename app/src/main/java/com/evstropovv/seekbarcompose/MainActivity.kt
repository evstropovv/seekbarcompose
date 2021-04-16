package com.evstropovv.seekbarcompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import com.evstropovv.seekbarcompose.ui.theme.SeekbarComposeTheme
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeekbarComposeTheme {
                Surface(color = MaterialTheme.colors.background) {

                    val position = MutableStateFlow(10L)
                    val duration = MutableStateFlow(100L)

                    Seekbar(
                        position = position.collectAsState(),
                        duration = duration.collectAsState(),
                        onNewProgress = { },
                        onDragStart = { },
                        onDragEnd = {
                            position.value = it
                            Toast.makeText(this, "New position: ${position.value}", Toast.LENGTH_SHORT).show()
                        })
                }
            }
        }
    }
}