package com.example.yournotes.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.yournotes.Database.NoteDatabase
import com.example.yournotes.Database.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application): AndroidViewModel(application) {

    private val repository:NotesRepository

    val allNotes: LiveData<List<Note>>

    init {

        val dao= NoteDatabase.getDatabase(application).getNotesDao()
        repository  = NotesRepository(dao)
        allNotes = repository.allNotes

    }
    fun insert(note: Note)= viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun deleteNode(note: Note)= viewModelScope.launch(Dispatchers.IO) {

        repository.delete(note)

    }

    fun updateNote(note : Note)= viewModelScope.launch(Dispatchers.IO) {

        repository.update(note)

    }

}