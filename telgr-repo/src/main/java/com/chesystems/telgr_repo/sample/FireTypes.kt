package com.chesystems.telgr_repo.sample

/**
 * Constants for Firestore collections and fields
 */
object FireTypes {
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