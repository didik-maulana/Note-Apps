package com.didik.noteapps.ui

import androidx.lifecycle.ViewModel
import com.didik.noteapps.data.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    val notes = noteRepository.notes
    val message = noteRepository.message
    val isLoading = noteRepository.isLoading

    fun getNotes() = CoroutineScope(Dispatchers.IO).launch {
        noteRepository.getNotes()
    }
}