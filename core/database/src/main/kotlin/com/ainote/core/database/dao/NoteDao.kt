package com.ainote.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ainote.core.database.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY updated_at DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE is_pinned = 1 ORDER BY updated_at DESC")
    fun getPinnedNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE note_id = :id")
    fun getNoteById(id: String): Flow<NoteEntity?>

    @Query("SELECT * FROM notes WHERE title = :title LIMIT 1")
    suspend fun getNoteByTitle(title: String): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)
    
    @Query("UPDATE notes SET is_archived = :isArchived WHERE note_id = :id")
    suspend fun updateArchiveStatus(id: String, isArchived: Boolean)

    @Query(
        """
        SELECT notes.* FROM notes 
        INNER JOIN note_tags ON notes.note_id = note_tags.note_id 
        WHERE note_tags.tag_id = :tagId AND notes.is_archived = 0
        ORDER BY notes.updated_at DESC
        """
    )
    fun getNotesByTagId(tagId: String): Flow<List<NoteEntity>>
}
