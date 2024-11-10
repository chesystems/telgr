package com.chesystems.telgr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.chesystems.telgr.ui.theme.TelgrTheme
import com.chesystems.telgr_data.Message
import com.chesystems.telgr_model.sample.ChatModel
import com.chesystems.uibits.EZIconButton
import com.chesystems.uibits.EZInput
import com.chesystems.uibits.RunOnce

class MainActivity : ComponentActivity() {
    private val chatMo by viewModels<ChatModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TelgrTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RunOnce {
                        chatMo.loadMessages("")
                    }

                    val (input, setInput) = remember { mutableStateOf("") }
                    Column(modifier = Modifier.padding(innerPadding)) {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = innerPadding
                        ) {
                            items(chatMo.messages) {
                                Text(text = it.content)
                            }
                        }
                        EZInput(name = input, setName = setInput, label = "Input...") {
                            EZIconButton(Icons.AutoMirrored.Outlined.Send) {
                                chatMo.sendMessage(
                                    Message(
                                        content = input
                                    )
                                )
                                setInput("")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TelgrTheme {

    }
}