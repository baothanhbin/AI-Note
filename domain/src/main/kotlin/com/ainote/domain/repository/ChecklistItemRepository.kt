package com.ainote.domain.repository

import com.ainote.core.model.note.ChecklistItem
import kotlinx.coroutines.flow.Flow

interface ChecklistItemRepository {
    fun getItemsForNote(noteId: String): Flow<List<ChecklistItem>>
    
    suspend fun saveItem(item: ChecklistItem)
    
    suspend fun deleteItem(item: ChecklistItem)
    
    suspend fun deleteItemsForNote(noteId: String)
}
