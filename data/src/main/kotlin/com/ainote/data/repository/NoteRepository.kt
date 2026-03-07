package com.ainote.data.repository

import com.ainote.core.model.note.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    
    fun getPinnedNotes(): Flow<List<Note>>
    
    fun getNoteById(id: String): Flow<Note?>
    
    suspend fun saveNote(note: Note)
    
    suspend fun updateNote(note: Note)
    
    suspend fun deleteNote(note: Note)
    
    suspend fun archiveNote(id: String, isArchived: Boolean)
}
