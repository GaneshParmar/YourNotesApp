package com.example.yournotes.Database

import androidx.lifecycle.LiveData
import com.example.yournotes.Models.Note

class NotesRepository(private val noteDao:Dao) {

    val allNotes: LiveData<List<Note>> = noteDao.getNotes()

    suspend fun insert(note :Note){
        noteDao.insert(note)
    }

    suspend fun delete(note: Note){
        noteDao.delete(note)
    }

    suspend fun update(note :Note){
        noteDao.update(note)
    }

}