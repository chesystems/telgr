package com.chesystems.telgr_model

typealias FireCollection = FireTypes.Collection
typealias FireChatField = FireTypes.Field.Chat

object FireTypes {
    /***/
    enum class Collection(val value: String) {
        MESSAGES("messages")
    }

    object Field {
        enum class Chat(val value: String) {
            GROUP_ID("groupId"),
            TIMESTAMP("timestamp")
        }
    }
}