package com.ainote.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ainote.core.database.model.NoteTagCrossRef
import com.ainote.core.database.model.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("SELECT * FROM tags")
    fun getAllTags(): Flow<List<TagEntity>>

    @Query(
        """
        SELECT tags.* FROM tags 
        INNER JOIN note_tags ON tags.tag_id = note_tags.tag_id 
        WHERE note_tags.note_id = :noteId
        """
    )
    fun getTagsForNote(noteId: String): Flow<List<TagEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: TagEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNoteTagCrossRef(crossRef: NoteTagCrossRef)

    @Delete
    suspend fun deleteNoteTagCrossRef(crossRef: NoteTagCrossRef)
    
    @Delete
    suspend fun deleteTag(tag: TagEntity)
}
