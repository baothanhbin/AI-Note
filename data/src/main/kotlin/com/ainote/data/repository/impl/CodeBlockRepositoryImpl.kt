package com.ainote.data.repository.impl

import com.ainote.core.database.dao.CodeBlockDao
import com.ainote.data.model.asDomainModel
import com.ainote.data.model.asEntityModel
import com.ainote.data.repository.CodeBlockRepository
import com.ainote.core.model.note.CodeBlock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CodeBlockRepositoryImpl @Inject constructor(
    private val dao: CodeBlockDao
) : CodeBlockRepository {

    override fun getCodeBlocksForNote(noteId: String): Flow<List<CodeBlock>> {
        return dao.getCodeBlocksForNote(noteId).map { entities ->
            entities.map { it.asDomainModel() }
        }
    }

    override suspend fun saveCodeBlock(block: CodeBlock) {
        dao.insertCodeBlock(block.asEntityModel())
    }

    override suspend fun deleteCodeBlock(block: CodeBlock) {
        dao.deleteCodeBlock(block.asEntityModel())
    }

    override suspend fun deleteCodeBlocksForNote(noteId: String) {
        dao.deleteCodeBlocksForNote(noteId)
    }
}
