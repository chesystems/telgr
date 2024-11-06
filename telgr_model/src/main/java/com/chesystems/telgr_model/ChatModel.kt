package com.chesystems.telgr_model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {
    private val chatRepository = ChatRepository()
    
    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message>
        get() = _messages

    private val _sendMessageStatus = mutableStateOf<Boolean?>(null)
    val sendMessageStatus: Boolean?
        get() = _sendMessageStatus.value

    fun loadMessages(groupId: String) {
        chatRepository.getMessagesForGroup(groupId) { messagesList ->
            _messages.clear()
            _messages.addAll(messagesList)
        }
    }

    fun sendMessage(message: Message) {
        chatRepository.sendMessage(message)
            .addOnSuccessListener {
                _sendMessageStatus.value = true
            }
            .addOnFailureListener {
                _sendMessageStatus.value = false
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
        _sendMessageStatus.value = null
    }

    override fun onCleared() {
        super.onCleared()
        chatRepository.stopListening()
    }
}

