package com.chesystems.telgr_model.sample

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chesystems.telgr_data.Message

/**
 * ViewModel for managing chat messages and their states
 */
class ChatModel : ViewModel() {
    private val chatRepository = ChatRepository()

    // Observable list of messages
    val messages = mutableStateListOf<Message>()
    // Tracks success/failure of message sending
    private var sendMessageStatus = mutableStateOf<Boolean?>(null)

    /**
     * Loads and observes messages for a specific group
     */
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

    /**
     * Sends a new message and updates status
     */
    fun sendMessage(message: Message) {
        chatRepository.sendMessage(message)
            .addOnSuccessListener {
                sendMessageStatus.value = true
            }
            .addOnFailureListener {
                sendMessageStatus.value = false
            }
    }

    /**
     * Resets send message status after handling
     */
    fun resetSendMessageStatus() { sendMessageStatus.value = null }

    /**
     * Cleanup when ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        chatRepository.stopListening()
    }
}
