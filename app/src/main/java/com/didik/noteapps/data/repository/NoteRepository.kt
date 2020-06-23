package com.didik.noteapps.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.didik.noteapps.data.models.Note
import com.didik.noteapps.extensions.post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository {
    private val firebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    val notes: LiveData<List<Note>> = MutableLiveData()
    val isLoading: LiveData<Boolean> = MutableLiveData()
    val message: LiveData<String> = MutableLiveData()

    suspend fun getNotes() = withContext(Dispatchers.IO) {
        isLoading.post(true)
        firebaseDatabase
            .reference
            .child(TABLE_NAME)
            .addValueEventListener(getNotesValueListener())
    }

    private fun getNotesValueListener() = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            isLoading.post(false)
            message.post("$error")
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            isLoading.post(false)

            val notesResult = mutableListOf<Note>()
            snapshot.children.forEach { dataSnapshot ->
                dataSnapshot.getValue(Note::class.java)?.let { note ->
                    note.key = dataSnapshot.key
                    notesResult.add(note)
                }
            }
            notes.post(notesResult)
        }
    }

    suspend fun insertNote(note: Note, isNoteInserted: (Boolean) -> Unit) =
        withContext(Dispatchers.IO) {
            isLoading.post(true)
            firebaseDatabase.reference.child(TABLE_NAME).push()
                .setValue(note)
                .addOnSuccessListener {
                    isLoading.post(false)
                    isNoteInserted(true)
                }
                .addOnFailureListener {
                    isLoading.post(false)
                    isNoteInserted(false)
                message.post("$it")
            }
    }

    suspend fun updateNote(note: Note, isNoteUpdated: (Boolean) -> Unit) =
        withContext(Dispatchers.IO) {
            isLoading.post(true)
            note.key?.let { key ->
                firebaseDatabase.reference.child(TABLE_NAME).child(key)
                    .setValue(note)
                    .addOnSuccessListener {
                        isLoading.post(true)
                        isNoteUpdated(true)
                    }
                    .addOnFailureListener {
                        isLoading.post(true)
                        isNoteUpdated(false)
                    message.post("$it")
                }
        }
    }

    suspend fun deleteNote(key: String, isNoteDeleted: (Boolean) -> Unit) =
        withContext(Dispatchers.IO) {
            isLoading.post(true)

            firebaseDatabase.reference.child(TABLE_NAME).child(key)
                .removeValue()
                .addOnSuccessListener {
                    isLoading.post(false)
                    isNoteDeleted(true)
                }
                .addOnFailureListener {
                    isLoading.post(false)
                    isNoteDeleted(false)
                message.post("$it")
            }
    }

    companion object {
        const val TABLE_NAME = "notes"
    }
}