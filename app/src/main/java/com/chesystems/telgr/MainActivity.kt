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
import com.chesystems.telgr_data.sample.Message
import com.chesystems.telgr_model.NotificationHelper
import com.chesystems.telgr_model.sample.ChatModel
import com.chesystems.uibits.EZIconButton
import com.chesystems.uibits.EZInput
import com.chesystems.uibits.RunOnce
import android.os.Build
import android.Manifest  // For permission checking
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat  // For permission checking
import androidx.core.app.ActivityCompat  // For requesting permissions

class MainActivity : ComponentActivity() {
    private val chatMo by viewModels<ChatModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        NotificationMgr.initialize(this)

        setContent {
            TelgrTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RunOnce {
                        chatMo.loadMessages("", NotificationMgr::showNotification)
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

object NotificationMgr {
    private var notificationHelper: NotificationHelper? = null

    fun initialize(activity: MainActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request permission
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }

        if (notificationHelper == null) {
            notificationHelper = NotificationHelper(activity.applicationContext)
        }
    }

    fun showNotification(
        notificationId: Int,
        title: String,
        message: String
    ) {
        notificationHelper?.showNotification(
            notificationId = notificationId,
            title = title,
            message = message
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TelgrTheme {

    }
}