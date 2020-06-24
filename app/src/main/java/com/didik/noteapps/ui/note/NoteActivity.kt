package com.didik.noteapps.ui.note

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.didik.noteapps.R
import com.didik.noteapps.data.models.Note
import com.didik.noteapps.data.repository.NoteRepository
import com.didik.noteapps.extensions.isShow
import com.didik.noteapps.extensions.isVisible
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
        initNoteAdapter()
        setupViewListener()
        initViewModel()
        observeNotes()
        setSearchListener()

        noteViewModel.getNotes()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.searchMenu -> {
                searchEditText.isShow(!searchEditText.isVisible())
                true
            }
            R.id.selectableMenu -> {
                if (noteAdapter.isNoteEmpty()) {
                    if (noteAdapter.isSelectableNote) {
                        item.setIcon(R.drawable.ic_check_items)
                    } else {
                        item.setIcon(R.drawable.ic_close)
                    }
                    noteAdapter.isSelectableNote = !noteAdapter.isSelectableNote
                } else {
                    showToast(getString(R.string.msg_note_is_empty))
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setupViewListener() {
        addNoteButton.setOnClickListener {
            startActivity(FormActivity.getIntent(this))
        }
    }

    private fun initNoteAdapter() {
        noteAdapter = NoteAdapter(this@NoteActivity, mutableListOf())
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
            notesRecyclerView.isShow(!notes.isNullOrEmpty())
            emptyTextView.isShow(notes.isNullOrEmpty())
        }

        observe(noteViewModel.isLoading) { isLoading ->
            setViewLoading(isLoading)
        }

        observe(noteViewModel.message) { message ->
            showToast(message)
        }
    }

    private fun setNoteAdapter(notes: List<Note>) {
        noteAdapter.setItems(notes.toMutableList())

        notesRecyclerView.apply {
            adapter = noteAdapter
            clipToPadding = false
        }

        noteAdapter.onItemClickListener = { note ->
            startActivity(FormActivity.getIntent(this, note))
        }
    }

    private fun setViewLoading(isShow: Boolean) {
        loadingProgressBar.isShow(isShow)
        notesRecyclerView.isShow(!isShow)
    }

    private fun setSearchListener() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(text: Editable?) {

            }
        })
    }
}