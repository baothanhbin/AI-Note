package com.ainote.core.model.note

import java.time.LocalDateTime

data class NoteLink(
    val fromNoteId: String,
    val toNoteId: String,
    val createdAt: LocalDateTime
)
