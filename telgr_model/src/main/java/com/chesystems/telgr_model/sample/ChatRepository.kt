package com.chesystems.telgr_model.sample

import com.chesystems.telgr_model.BaseFirestoreRepository
import com.chesystems.telgr_data.Message
import com.google.android.gms.tasks.Task

class ChatRepository : BaseFirestoreRepository<Message>(
    collectionPath = FireTypes.Collection.MESSAGES.value,
    typeClass = Message::class.java
) {
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

    fun sendMessage(message: Message): Task<Void> {
        return add(message)
    }
}