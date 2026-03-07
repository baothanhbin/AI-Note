package com.ainote.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ainote.core.database.model.CodeBlockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CodeBlockDao {
    @Query("SELECT * FROM code_blocks WHERE note_id = :noteId ORDER BY order_index ASC")
    fun getCodeBlocksForNote(noteId: String): Flow<List<CodeBlockEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCodeBlock(block: CodeBlockEntity)

    @Update
    suspend fun updateCodeBlock(block: CodeBlockEntity)

    @Delete
    suspend fun deleteCodeBlock(block: CodeBlockEntity)

    @Query("DELETE FROM code_blocks WHERE note_id = :noteId")
    suspend fun deleteCodeBlocksForNote(noteId: String)
}
