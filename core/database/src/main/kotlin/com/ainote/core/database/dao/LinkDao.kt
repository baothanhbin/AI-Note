package com.ainote.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ainote.core.database.model.NoteLinkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLink(link: NoteLinkEntity)

    @Delete
    suspend fun deleteLink(link: NoteLinkEntity)

    @Query("DELETE FROM note_links WHERE from_note_id = :noteId")
    suspend fun deleteOutgoingLinks(noteId: String)

    @Query("SELECT * FROM note_links WHERE from_note_id = :noteId")
    fun getOutgoingLinks(noteId: String): Flow<List<NoteLinkEntity>>

    @Query("SELECT * FROM note_links WHERE to_note_id = :noteId")
    fun getBacklinks(noteId: String): Flow<List<NoteLinkEntity>>
    
    @Query("SELECT * FROM note_links")
    fun getAllLinks(): Flow<List<NoteLinkEntity>>
}
