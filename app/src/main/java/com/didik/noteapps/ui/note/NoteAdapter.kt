package com.didik.noteapps.ui.note

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.didik.noteapps.R
import com.didik.noteapps.data.models.Note
import kotlinx.android.synthetic.main.item_note.view.*

class NoteAdapter(
    private val context: Context,
    private val notes: MutableList<Note>
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    var onItemClickListener: (note: Note) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bindView(notes[position])
    }


    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(note: Note) {
            with(itemView) {
                titleNoteTextView.text = note.title
                descriptionNoteTextView.text = note.description

                setOnClickListener {
                    onItemClickListener(note)
                }
            }
        }
    }
}