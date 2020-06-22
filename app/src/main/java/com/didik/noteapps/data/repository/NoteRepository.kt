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

    companion object {
        const val TABLE_NAME = "notes"
    }
}