package com.didik.noteapps.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.didik.noteapps.data.models.Note
import com.didik.noteapps.data.repository.NoteRepository
import com.didik.noteapps.extensions.post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    var note: Note? = null

    val notes = noteRepository.notes
    val message = noteRepository.message
    val isLoading = noteRepository.isLoading
    val isNoteInserted: LiveData<Boolean> = MutableLiveData()
    val isNoteUpdated: LiveData<Boolean> = MutableLiveData()
    val isNoteDeleted: LiveData<Boolean> = MutableLiveData()

    fun getNotes() = CoroutineScope(Dispatchers.IO).launch {
        noteRepository.getNotes()
    }

    fun insertNote() = CoroutineScope(Dispatchers.IO).launch {
        note?.let {
            noteRepository.insertNote(it) { isSuccess ->
                isNoteInserted.post(isSuccess)
            }
        }
    }

    fun updateNote() = CoroutineScope(Dispatchers.IO).launch {
        note?.let {
            noteRepository.updateNote(it) { isSuccess ->
                isNoteUpdated.post(isSuccess)
            }
        }
    }

    fun deleteNote() = CoroutineScope(Dispatchers.IO).launch {
        note?.key?.let { key ->
            noteRepository.deleteNote(key) { isSuccess ->
                isNoteDeleted.post(isSuccess)
            }
        }
    }
}