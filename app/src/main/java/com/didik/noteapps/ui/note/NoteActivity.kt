package com.didik.noteapps.ui.note

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.didik.noteapps.R
import com.didik.noteapps.data.models.Note
import com.didik.noteapps.data.repository.NoteRepository
import com.didik.noteapps.extensions.isShow
import com.didik.noteapps.extensions.observe
import com.didik.noteapps.extensions.showToast
import com.didik.noteapps.ui.NoteViewModel
import com.didik.noteapps.ui.ViewModelFactory
import com.didik.noteapps.ui.form.FormActivity
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setupViewListener()
        initViewModel()
        observeNotes()

        noteViewModel.getNotes()
    }

    private fun setupViewListener() {
        addNoteButton.setOnClickListener {
            startActivity(FormActivity.getIntent(this))
        }
    }

    private fun initViewModel() {
        noteViewModel = ViewModelProvider(
            this,
            ViewModelFactory(NoteRepository())
        ).get(NoteViewModel::class.java)
    }

    private fun observeNotes() {
        observe(noteViewModel.notes) { notes ->
            setNoteAdapter(notes)
        }

        observe(noteViewModel.isLoading) { isLoading ->
            setViewLoading(isLoading)
        }

        observe(noteViewModel.message) { message ->
            showToast(message)
        }
    }

    private fun setViewLoading(isShow: Boolean) {
        loadingProgressBar.isShow(isShow)
        notesRecyclerView.isShow(!isShow)
    }

    private fun setNoteAdapter(notes: List<Note>) {
        noteAdapter = NoteAdapter(this@NoteActivity, notes.toMutableList())
        notesRecyclerView.apply {
            adapter = noteAdapter
            clipToPadding = false
        }

        noteAdapter.onItemClickListener = { note ->
            startActivity(FormActivity.getIntent(this, note))
        }
    }
}