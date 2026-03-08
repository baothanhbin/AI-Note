package com.ainote.data.repository.impl

import com.ainote.core.database.dao.ChecklistItemDao
import com.ainote.data.model.asDomainModel
import com.ainote.data.model.asEntityModel
import com.ainote.domain.repository.ChecklistItemRepository
import com.ainote.core.model.note.ChecklistItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChecklistItemRepositoryImpl @Inject constructor(
    private val dao: ChecklistItemDao
) : ChecklistItemRepository {

    override fun getItemsForNote(noteId: String): Flow<List<ChecklistItem>> {
        return dao.getItemsForNote(noteId).map { entities ->
            entities.map { it.asDomainModel() }
        }
    }

    override suspend fun saveItem(item: ChecklistItem) {
        dao.insertItem(item.asEntityModel())
    }

    override suspend fun deleteItem(item: ChecklistItem) {
        dao.deleteItem(item.asEntityModel())
    }

    override suspend fun deleteItemsForNote(noteId: String) {
        dao.deleteItemsForNote(noteId)
    }
}
