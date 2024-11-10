package com.chesystems.telgr_model.sample

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chesystems.telgr_data.Message

class ChatModel : ViewModel() {
    private val chatRepository = ChatRepository()

    val messages = mutableStateListOf<Message>()
    private var sendMessageStatus = mutableStateOf<Boolean?>(null)

    fun loadMessages(groupId: String) {
        chatRepository.getMessagesForGroup(groupId,
            onMessageAdded = { message -> 
                messages.add(message)
            },
            onMessageModified = { message ->
                val index = messages.indexOfFirst { it.id == message.id }
                if (index != -1) {
                    messages[index] = message
                }
            },
            onMessageRemoved = { messageId ->
                messages.removeAll { it.id == messageId }
            }
        )
    }

    fun sendMessage(message: Message) {
        chatRepository.sendMessage(message)
            .addOnSuccessListener {
                sendMessageStatus.value = true
            }
            .addOnFailureListener {
                sendMessageStatus.value = false
            }
    }

    // Reset status after handling
    fun resetSendMessageStatus() { sendMessageStatus.value = null }

    override fun onCleared() {
        super.onCleared()
        chatRepository.stopListening()
    }
}

