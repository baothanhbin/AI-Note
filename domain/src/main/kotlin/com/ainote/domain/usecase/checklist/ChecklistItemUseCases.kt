package com.ainote.domain.usecase.checklist

import com.ainote.core.model.note.ChecklistItem
import com.ainote.data.repository.ChecklistItemRepository
import javax.inject.Inject
import java.util.UUID

class SaveChecklistItemUseCase @Inject constructor(
    private val repository: ChecklistItemRepository
) {
    suspend operator fun invoke(noteId: String, content: String, orderIndex: Int) {
        val item = ChecklistItem(
            itemId = UUID.randomUUID().toString(),
            noteId = noteId,
            content = content,
            isChecked = false,
            orderIndex = orderIndex
        )
        repository.saveItem(item)
    }
}

class UpdateChecklistItemUseCase @Inject constructor(
    private val repository: ChecklistItemRepository
) {
    suspend operator fun invoke(item: ChecklistItem) {
        repository.saveItem(item)
    }
}

class DeleteChecklistItemUseCase @Inject constructor(
    private val repository: ChecklistItemRepository
) {
    suspend operator fun invoke(item: ChecklistItem) {
        repository.deleteItem(item)
    }
}
