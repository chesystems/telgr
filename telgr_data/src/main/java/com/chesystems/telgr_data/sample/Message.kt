package com.chesystems.telgr_data.sample

import com.chesystems.telgr_data.FirestoreModel

/**
 * Represents a message in the chat system.
 * Implements FirestoreModel for database storage.
 * Contains message content, metadata and type information.
 */
data class Message(
    override val id: String = "",  // Firestore document ID
    override val timestamp: Long = System.currentTimeMillis(),
    val senderId: String = "",  // User ID who sent the message
    val content: String = "",
    val groupId: String = "",  // ID of the group/conversation this message belongs to
    val type: MessageType = MessageType.TEXT
): FirestoreModel {
    enum class MessageType {
        TEXT,
        IMAGE,
        // Add more types as needed
    }
} 