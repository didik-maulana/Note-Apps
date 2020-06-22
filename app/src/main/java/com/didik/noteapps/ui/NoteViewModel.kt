package com.didik.noteapps.ui

import androidx.lifecycle.ViewModel
import com.didik.noteapps.data.models.Note
import com.didik.noteapps.data.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    val notes = noteRepository.notes
    val message = noteRepository.message
    val isLoading = noteRepository.isLoading
    val isNoteInserted = noteRepository.isNoteInserted
    val isNoteUpdated = noteRepository.isNoteUpdated
    val isNoteDeleted = noteRepository.isNoteDeleted

    fun getNotes() = CoroutineScope(Dispatchers.IO).launch {
        noteRepository.getNotes()
    }

    fun insertNote(note: Note) = CoroutineScope(Dispatchers.IO).launch {
        noteRepository.insertNote(note)
    }

    fun updateNote(note: Note) = CoroutineScope(Dispatchers.IO).launch {
        noteRepository.updateNote(note)
    }

    fun deleteNote(key: String) = CoroutineScope(Dispatchers.IO).launch {
        noteRepository.deleteNote(key)
    }
}