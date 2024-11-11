package com.chesystems.telgr_repo.sample

import com.chesystems.telgr_repo.model.BaseFirestoreRepository
import com.google.android.gms.tasks.Task

/**
 * Constants for Firestore collections and fields
 */
internal object FireTypes {
    /**
     * Firestore collection paths
     */
    enum class Collection(val value: String) {
        MESSAGES("messages")
    }

    /**
     * Firestore field names
     */
    object Field {
        /**
         * Chat document fields
         */
        enum class Chat(val value: String) {
            GROUP_ID("groupId"),
            TIMESTAMP("timestamp")
        }
    }
}

/**
 * Repository for managing chat messages in Firestore.
 * Extends BaseFirestoreRepository for basic CRUD operations.
 */
class ChatRepository : BaseFirestoreRepository<Message>(
    collectionPath = FireTypes.Collection.MESSAGES.value,
    typeClass = Message::class.java
) {
    /**
     * Listens for messages in a specific group, ordered by timestamp.
     * Provides callbacks for message additions, modifications and removals.
     */
    fun getMessagesForGroup(
        groupId: String,
        onMessageAdded: (Message) -> Unit,
        onMessageModified: (Message) -> Unit,
        onMessageRemoved: (Message) -> Unit
    ) {
        startListening({ collection ->
            collection
                .whereEqualTo(FireTypes.Field.Chat.GROUP_ID.value, groupId)
                .orderBy(FireTypes.Field.Chat.TIMESTAMP.value)
        }, onMessageAdded, onMessageModified, onMessageRemoved)
    }

    /**
     * Adds a new message to Firestore.
     */
    fun sendMessage(message: Message): Task<Void> {
        return add(message)
    }
}