package com.didik.noteapps.ui.form

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.didik.noteapps.R
import com.didik.noteapps.data.models.Note
import com.didik.noteapps.data.repository.NoteRepository
import com.didik.noteapps.extensions.*
import com.didik.noteapps.ui.NoteViewModel
import com.didik.noteapps.ui.ViewModelFactory
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        initViewModel()
        setNoteFromIntent()
        setupViewListener()
        observeFormAction()
    }

    private fun initViewModel() {
        noteViewModel = ViewModelProvider(
            this,
            ViewModelFactory(NoteRepository())
        ).get(NoteViewModel::class.java)
    }

    private fun observeFormAction() {
        observe(noteViewModel.isLoading) { isLoading ->
            loadingProgressBar.isShow(isLoading)
            formGroup.isShow(!isLoading)
        }

        observe(noteViewModel.message) { message ->
            showToast(message)
        }

        observe(noteViewModel.isNoteInserted) { isNoteInserted ->
            if (isNoteInserted) {
                showToast(getString(R.string.msg_success_note_inserted))
                finish()
            }
        }

        observe(noteViewModel.isNoteUpdated) { isNoteUpdated ->
            if (isNoteUpdated) {
                showToast(getString(R.string.msg_success_updated))
                finish()
            }
        }

        observe(noteViewModel.isNoteDeleted) { isNoteDeleted ->
            if (isNoteDeleted) {
                showToast(getString(R.string.msg_success_note_deleted))
                finish()
            }
        }
    }

    private fun setNoteFromIntent() {
        if (intent?.hasExtra(EXTRA_NOTE) == true) {
            noteViewModel.note = intent?.getParcelableExtra(EXTRA_NOTE)

            noteViewModel.note?.run {
                titleEditText.setText(title)
                descriptionEditText.setText(description)
                deleteButton.isShow(!key.isNullOrBlank())

                submitButton.text = getString(R.string.action_update)
            }
        } else {
            noteViewModel.note = Note()
        }
    }

    private fun setupViewListener() {
        submitButton.setOnClickListener {
            validateForm()
        }

        deleteButton.setOnClickListener {
            noteViewModel.deleteNote()
        }
    }

    private fun validateForm() {
        when {
            titleEditText.isBlank() -> {
                showToast(getString(R.string.msg_error_title_required))
            }
            descriptionEditText.isBlank() -> {
                showToast(getString(R.string.msg_error_description_required))
            }
            else -> {
                sendNote()
            }
        }
    }

    private fun sendNote() {
        noteViewModel.note?.apply {
            title = titleEditText.getValue()
            description = descriptionEditText.getValue()
        }

        if (noteViewModel.note?.key.isNullOrBlank()) {
            noteViewModel.insertNote()
        } else {
            noteViewModel.updateNote()
        }
    }

    companion object {
        private const val EXTRA_NOTE = "extra_note"

        fun getIntent(context: Context?, note: Note? = null): Intent {
            return Intent(context, FormActivity::class.java).apply {
                note?.let {
                    putExtra(EXTRA_NOTE, it)
                }
            }
        }
    }
}