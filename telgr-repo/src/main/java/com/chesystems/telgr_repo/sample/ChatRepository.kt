package com.chesystems.telgr_repo.sample

import com.chesystems.telgr_repo.model.BaseFirestoreRepository
import com.google.android.gms.tasks.Task

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
        onMessageRemoved: (String) -> Unit
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