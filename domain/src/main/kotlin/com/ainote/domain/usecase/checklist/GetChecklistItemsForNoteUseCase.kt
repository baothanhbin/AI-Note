package com.ainote.domain.usecase.checklist

import com.ainote.core.model.note.ChecklistItem
import com.ainote.domain.repository.ChecklistItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChecklistItemsForNoteUseCase @Inject constructor(
    private val repository: ChecklistItemRepository
) {
    operator fun invoke(noteId: String): Flow<List<ChecklistItem>> {
        return repository.getItemsForNote(noteId)
    }
}
