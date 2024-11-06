package com.chesystems.telgr_model

data class Message(
    val id: String = "",  // Firestore document ID
    val senderId: String = "",  // User ID who sent the message
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val groupId: String = "",  // ID of the group/conversation this message belongs to
    val type: MessageType = MessageType.TEXT
) {
    enum class MessageType {
        TEXT,
        IMAGE,
        // Add more types as needed
    }
} 