package com.ainote.data.model

import com.ainote.core.database.model.ChecklistItemEntity
import com.ainote.core.model.note.ChecklistItem

fun ChecklistItemEntity.asDomainModel() = ChecklistItem(
    itemId = itemId,
    noteId = noteId,
    content = content,
    isChecked = isChecked,
    orderIndex = orderIndex
)

fun ChecklistItem.asEntityModel() = ChecklistItemEntity(
    itemId = itemId,
    noteId = noteId,
    content = content,
    isChecked = isChecked,
    orderIndex = orderIndex
)
