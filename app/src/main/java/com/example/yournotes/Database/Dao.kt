package com.example.yournotes.Database

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yournotes.Models.Note
@androidx.room.Dao
interface Dao {

    //suspend --> for co routines it should be defined as suspend
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("Select * from notes_table order by id ASC")
    fun getNotes(): LiveData<List<Note>>

    @Update
    suspend fun update(note: Note)
}
