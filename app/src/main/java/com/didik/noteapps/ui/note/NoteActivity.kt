package com.didik.noteapps.ui.note

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.didik.noteapps.R
import com.didik.noteapps.extensions.isShow
import com.didik.noteapps.extensions.showToast
import com.didik.noteapps.models.Note
import com.didik.noteapps.ui.form.FormActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {
    private val firebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setupViewListener()
        loadNoteFromDatabase()
    }

    private fun setupViewListener() {
        addNoteButton.setOnClickListener {
            startActivity(FormActivity.getIntent(this))
        }
    }

    private fun loadNoteFromDatabase() {
        setViewLoading(true)

        firebaseDatabase
            .reference
            .child("notes")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    setViewLoading(false)
                    showToast(getString(R.string.msg_error_failed_fetch_note_from_database))
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val notes = mutableListOf<Note>()

                    snapshot.children.forEach { dataSnapshot ->
                        dataSnapshot.getValue(Note::class.java)?.let { note ->
                            note.key = dataSnapshot.key
                            notes.add(note)
                        }
                    }

                    setNoteAdapter(notes)
                    setViewLoading(false)
                }
            })
    }

    private fun setViewLoading(isShow: Boolean) {
        loadingProgressBar.isShow(isShow)
        notesRecyclerView.isShow(!isShow)
    }

    private fun setNoteAdapter(notes: MutableList<Note>) {
        noteAdapter = NoteAdapter(this@NoteActivity, notes)
        notesRecyclerView.apply {
            adapter = noteAdapter
            clipToPadding = false
        }

        noteAdapter.onItemClickListener = { note ->
            startActivity(FormActivity.getIntent(this, note))
        }
    }
}