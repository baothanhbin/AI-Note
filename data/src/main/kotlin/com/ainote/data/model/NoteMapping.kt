package com.ainote.data.model

import com.ainote.core.database.model.NoteEntity
import com.ainote.core.model.note.Note
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

fun NoteEntity.asDomainModel(): Note = Note(
    id = id,
    title = title,
    content = content,
    type = type,
    createdAt = java.time.LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault()),
    updatedAt = java.time.LocalDateTime.ofInstant(Instant.ofEpochMilli(updatedAt), ZoneId.systemDefault()),
    isPinned = isPinned,
    isArchived = isArchived
)

fun Note.asEntityModel(): NoteEntity = NoteEntity(
    id = id,
    title = title,
    content = content,
    type = type,
    createdAt = createdAt.toInstant(ZoneOffset.UTC).toEpochMilli(),
    updatedAt = updatedAt.toInstant(ZoneOffset.UTC).toEpochMilli(),
    isPinned = isPinned,
    isArchived = isArchived
)
