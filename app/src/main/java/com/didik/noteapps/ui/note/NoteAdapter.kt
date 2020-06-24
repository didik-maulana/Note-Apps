package com.didik.noteapps.ui.note

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.didik.noteapps.R
import com.didik.noteapps.data.models.Note
import com.didik.noteapps.extensions.isShow
import kotlinx.android.synthetic.main.item_note.view.*

class NoteAdapter(
    private val context: Context,
    private val notes: MutableList<Note>
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    var isSelectableNote: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClickListener: (note: Note) -> Unit = {}

    fun setItems(notes: MutableList<Note>) {
        this.notes.apply {
            clear()
            addAll(notes)
        }
        notifyDataSetChanged()
    }

    fun isNoteEmpty(): Boolean {
        return itemCount > 0
    }

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
                noteCheckBox.isShow(isSelectableNote)

                titleNoteTextView.text = note.title
                descriptionNoteTextView.text = note.description

                setOnClickListener {
                    onItemClickListener(note)
                }
            }
        }
    }
}