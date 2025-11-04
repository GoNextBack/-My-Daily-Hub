package com.example.mydailyhub.features.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.Instant

data class NoteEntry(
    val id: Long,
    val body: String,
    val createdAt: Instant,
)

class NotesViewModel : ViewModel() {

    var noteInput by mutableStateOf("")
        private set

    private val _notes = mutableStateListOf<NoteEntry>()
    val notes: List<NoteEntry> get() = _notes

    fun onNoteInputChanged(value: String) {
        noteInput = value
    }

    fun addNote() {
        val content = noteInput.trim()
        if (content.isEmpty()) return
        val entry = NoteEntry(
            id = System.currentTimeMillis(),
            body = content,
            createdAt = Instant.now(),
        )
        _notes.add(index = 0, element = entry)
        noteInput = ""
    }

    fun removeNote(id: Long) {
        _notes.removeAll { it.id == id }
    }
}
