package com.chesystems.telgr_data

/**
 * Base interface for Firestore document models.
 * Defines common properties for all Firestore documents.
 */
interface FirestoreModel {
    val id: String        // Document ID in Firestore
    val timestamp: Long   // Document timestamp
}