package com.chesystems.telgr_model

import com.google.android.gms.tasks.Task

class ChatRepository : BaseFirestoreRepository<Message>(
    collectionPath = "messages",
    clazz = Message::class.java
) {
    fun getMessagesForGroup(
        groupId: String,
        onMessagesLoaded: (List<Message>) -> Unit
    ) {
        startListening({ collection ->
            collection
                .whereEqualTo("groupId", groupId)
                //.orderBy("timestamp")
        }, onMessagesLoaded)
    }

    fun sendMessage(message: Message): Task<Void> {
        return add(message)
    }

    fun deleteMessage(messageId: String): Task<Void> {
        return delete(messageId)
    }

    fun updateMessageContent(messageId: String, newContent: String): Task<Void> {
        return update(messageId, mapOf(
            "content" to newContent
        ))
    }
}