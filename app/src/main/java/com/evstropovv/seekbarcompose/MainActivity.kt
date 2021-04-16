package com.evstropovv.seekbarcompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.evstropovv.seekbarcompose.ui.theme.SeekbarComposeTheme
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeekbarComposeTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {

    Column {

        repeat(10) {

            val context = LocalContext.current

            val position = MutableStateFlow(10L)
            val duration = MutableStateFlow(100L)

            Seekbar(
                position = position.collectAsState(),
                duration = duration.collectAsState(),
                onNewProgress = { },
                onDragStart = { },
                onDragEnd = {
                    position.value = it
                    Toast.makeText(context, "New position: ${position.value}", Toast.LENGTH_SHORT).show()
                })

            Spacer(modifier = Modifier.height(20.dp))

        }
    }
}

@Preview
@Composable
fun MainScreenPreview(){
    SeekbarComposeTheme {
        MainScreen()
    }
}