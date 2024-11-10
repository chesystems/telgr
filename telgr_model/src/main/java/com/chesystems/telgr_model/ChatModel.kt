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
        chatRepository.getMessagesForGroup(groupId) { messagesList ->
            messages.clear()
            messages.addAll(messagesList)
        }
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

    fun deleteMessage(messageId: String) {
        chatRepository.deleteMessage(messageId)
    }

    fun updateMessageContent(messageId: String, newContent: String) {
        chatRepository.updateMessageContent(messageId, newContent)
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

