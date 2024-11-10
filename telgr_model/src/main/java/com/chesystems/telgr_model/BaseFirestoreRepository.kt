package com.chesystems.telgr_model

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentChange.Type.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

abstract class BaseFirestoreRepository<T: Any>(
    protected val collectionPath: String,
    protected val clazz: Class<T>
) {
    private val db: FirebaseFirestore = Firebase.firestore
    private var listenerRegistration: ListenerRegistration? = null
    
    private val collection: CollectionReference
        get() = db.collection(collectionPath)

    open fun startListening(
        queryBuilder: (CollectionReference) -> Query = { it },
        onDataAdded: (List<T>) -> Unit,
        onDataModified: (List<T>) -> Unit,
        onDataRemoved: () -> Unit
    ) {
        listenerRegistration?.remove()
        
        val query = queryBuilder(collection)
        listenerRegistration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }
            
            snapshot?.documents?.mapNotNull {
                it.toObject(clazz)
            }?.let {
                for (dc in snapshot.documentChanges) {
                    when (dc.type) {
                        ADDED -> onDataAdded(it)
                        MODIFIED -> onDataModified(it)
                        REMOVED -> onDataRemoved()
                    }
                }
            }
        }
    }

    open fun stopListening() {
        listenerRegistration?.remove()
        listenerRegistration = null
    }

    open fun add(item: T, documentId: String? = null): Task<Void> {
        return if (documentId != null) {
            collection.document(documentId).set(item)
        } else {
            collection.document().set(item)
        }
    }

    open fun update(documentId: String, updates: Map<String, Any>): Task<Void> {
        return collection.document(documentId).update(updates)
    }

    open fun delete(documentId: String): Task<Void> {
        return collection.document(documentId).delete()
    }

    open fun getById(documentId: String): Task<DocumentSnapshot> {
        return collection.document(documentId).get()
    }

    protected open fun onError(error: Exception) {
        // Override this to handle errors
        Log.e("FirestoreRepository", "Error: ${error.message}", error)
    }
} 