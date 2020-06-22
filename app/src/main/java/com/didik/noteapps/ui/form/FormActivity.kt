package com.didik.noteapps.ui.form

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.didik.noteapps.R
import com.didik.noteapps.data.models.Note
import com.didik.noteapps.extensions.getValue
import com.didik.noteapps.extensions.isBlank
import com.didik.noteapps.extensions.isShow
import com.didik.noteapps.extensions.showToast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : AppCompatActivity() {
    private val firebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        setNoteFromIntent()
        setupViewListener()
    }

    private fun setNoteFromIntent() {
        if (intent?.hasExtra(EXTRA_NOTE) == true) {
            note = intent?.getParcelableExtra(EXTRA_NOTE)

            titleEditText.setText(note?.title)
            descriptionEditText.setText(note?.description)
            deleteButton.isShow(!note?.key.isNullOrBlank())
        }
    }

    private fun setupViewListener() {
        submitButton.setOnClickListener {
            validateForm()
        }

        deleteButton.setOnClickListener {
            deleteNote()
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

    private fun setShowLoading(isShow: Boolean) {
        loadingProgressBar.isShow(isShow)
        formGroup.isShow(!isShow)
    }

    private fun sendNote() {
        setShowLoading(true)

        note?.apply {
            title = titleEditText.getValue()
            description = descriptionEditText.getValue()
        }

        if (note?.key.isNullOrBlank()) {
            insertNote()
        } else {
            updateNote()
        }
    }

    private fun insertNote() {
        firebaseDatabase.reference.child("notes").push()
            .setValue(note)
            .addOnSuccessListener {
                showToast("Note saved to database")
            }
            .addOnFailureListener {
                showToast("Failed, $it")
            }
    }

    private fun updateNote() {
        note?.key?.let { key ->
            firebaseDatabase.reference.child("notes").child(key)
                .setValue(note)
                .addOnSuccessListener {
                    setShowLoading(false)
                    showToast("Note updated to database")
                }
                .addOnFailureListener {
                    setShowLoading(false)
                    showToast("Failed update note, $it")
                }
        }
    }

    private fun deleteNote() {
        setShowLoading(true)

        note?.key?.let { key ->
            firebaseDatabase.reference.child("notes").child(key)
                .removeValue()
                .addOnSuccessListener {
                    setShowLoading(false)
                    showToast("Note deleted from database")
                    finish()
                }
                .addOnFailureListener {
                    setShowLoading(false)
                    showToast("Failed delete note, $it")
                }
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