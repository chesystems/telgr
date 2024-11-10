package com.chesystems.telgr_model

import android.util.Log
import com.chesystems.telgr_data.FirestoreModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange.Type.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * Base repository class for Firestore operations.
 * Provides common CRUD operations and real-time updates for Firestore documents.
 * @param T The model type that implements FirestoreModel
 * @param collectionPath The Firestore collection path
 * @param typeClass The class object of type T for deserialization
 */
abstract class BaseFirestoreRepository<T: FirestoreModel>(
    protected val collectionPath: String,
    protected val typeClass: Class<T>
) {
    private val db: FirebaseFirestore = Firebase.firestore
    private var listenerRegistration: ListenerRegistration? = null
    
    private val collection: CollectionReference
        get() = db.collection(collectionPath)

    /**
     * Starts real-time listening for collection changes.
     * @param queryBuilder Optional query modifier
     * @param onAdded Callback for document additions
     * @param onModified Callback for document modifications
     * @param onRemoved Callback for document deletions
     */
    open fun startListening(
        queryBuilder: (CollectionReference) -> Query = { it },
        onAdded: (T) -> Unit,
        onModified: (T) -> Unit,
        onRemoved: (String) -> Unit
    ) {
        listenerRegistration?.remove()
        
        val query = queryBuilder(collection)
        listenerRegistration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }
            
            snapshot?.documentChanges?.forEach { change ->
                when (change.type) {
                    ADDED -> change.document.toObject(typeClass)?.let(onAdded)
                    MODIFIED -> change.document.toObject(typeClass)?.let(onModified)
                    REMOVED -> onRemoved(change.document.id)
                }
            }
        }
    }

    /**
     * Stops listening to collection changes and cleans up resources
     */
    open fun stopListening() {
        listenerRegistration?.remove()
        listenerRegistration = null
    }

    /**
     * Adds a new document to the collection
     * @param item The item to add
     * @param documentId Optional custom document ID
     * @return Task representing the operation
     */
    open fun add(item: T, documentId: String? = null): Task<Void> {
        return if (documentId != null) {
            collection.document(documentId).set(item)
        } else {
            collection.document().set(item)
        }
    }

    /**
     * Updates an existing document
     * @param documentId ID of document to update
     * @param updates Map of field updates
     * @return Task representing the operation
     */
    open fun update(documentId: String, updates: Map<String, Any>): Task<Void> {
        return collection.document(documentId).update(updates)
    }

    /**
     * Deletes a document from the collection
     * @param documentId ID of document to delete
     * @return Task representing the operation
     */
    open fun delete(documentId: String): Task<Void> {
        return collection.document(documentId).delete()
    }

    /**
     * Retrieves a document by its ID
     * @param documentId ID of document to retrieve
     * @return Task containing the document snapshot
     */
    open fun getById(documentId: String): Task<DocumentSnapshot> {
        return collection.document(documentId).get()
    }

    /**
     * Handles repository errors
     * Override this method to implement custom error handling
     * @param error The exception that occurred
     */
    protected open fun onError(error: Exception) {
        // Override this to handle errors
        Log.e("FirestoreRepository", "Error: ${error.message}", error)
    }
} 