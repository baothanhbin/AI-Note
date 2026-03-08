package com.ainote.domain.repository

import com.ainote.core.model.note.CodeBlock
import kotlinx.coroutines.flow.Flow

interface CodeBlockRepository {
    fun getCodeBlocksForNote(noteId: String): Flow<List<CodeBlock>>
    
    suspend fun saveCodeBlock(block: CodeBlock)
    
    suspend fun deleteCodeBlock(block: CodeBlock)
    
    suspend fun deleteCodeBlocksForNote(noteId: String)
}
