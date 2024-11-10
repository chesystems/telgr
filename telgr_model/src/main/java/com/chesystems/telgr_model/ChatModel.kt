package com.chesystems.telgr_model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {
    private val chatRepository = ChatRepository()
    
    val messages = mutableStateListOf<Message>()

    var sendMessageStatus = mutableStateOf<Boolean?>(null)
        private set

    fun loadMessages(groupId: String) {
        chatRepository.getMessagesForGroup(groupId,
            onDataAdded = { messages.clear(); messages.addAll(it) },
            onDataModified = {
                messages.addAll(it.filterNot { m -> m in messages })
            },
            onDataRemoved = { messages.clear() })
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
    fun resetSendMessageStatus() {
        sendMessageStatus.value = null
    }

    override fun onCleared() {
        super.onCleared()
        chatRepository.stopListening()
    }
}

