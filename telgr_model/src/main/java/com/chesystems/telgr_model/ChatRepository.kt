package com.chesystems.telgr_model

import com.google.android.gms.tasks.Task

class ChatRepository : BaseFirestoreRepository<Message>(
    collectionPath = FireCollection.MESSAGES.value,
    clazz = Message::class.java
) {
    fun getMessagesForGroup(
        groupId: String,
        onDataAdded: (List<Message>) -> Unit,
        onDataModified: (List<Message>) -> Unit,
        onDataRemoved: () -> Unit
    ) {
        startListening({ collection ->
            collection
                .whereEqualTo(FireChatField.GROUP_ID.value, groupId)
                .orderBy(FireChatField.TIMESTAMP.value)
        }, onDataAdded, onDataModified, onDataRemoved)
    }

    fun sendMessage(message: Message): Task<Void> {
        return add(message)
    }
}