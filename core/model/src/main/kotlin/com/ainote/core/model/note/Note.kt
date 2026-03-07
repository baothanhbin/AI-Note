package com.ainote.core.model.note

import java.time.LocalDateTime

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val type: NoteType,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isPinned: Boolean,
    val isArchived: Boolean
)
