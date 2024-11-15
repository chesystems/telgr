package com.chesystems.telgr_repo.sample

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

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
    fun loadMessages(groupId: String, showNotification: (Int, String, String) -> Unit) {
        chatRepository.getMessagesForGroup(groupId,
            onMessageAdded = { message -> 
                messages.add(message)
                showNotification(1, message.groupId, message.content)
            },
            onMessageModified = { message ->
                val index = messages.indexOfFirst { it.id == message.id }
                if (index != -1) {
                    messages[index] = message
                }
            },
            onMessageRemoved = { message ->
                messages.removeAll { it.id == message.id }
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
