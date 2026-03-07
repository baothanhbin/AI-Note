package com.ainote.core.model.note

data class ChecklistItem(
    val id: String,
    val noteId: String,
    val content: String,
    val isChecked: Boolean,
    val orderIndex: Int
)
