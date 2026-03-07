package com.ainote.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

@Entity(tableName = "notes_fts")
@Fts4(contentEntity = NoteEntity::class)
data class NoteFtsEntity(
    @ColumnInfo(name = "note_id")
    val noteId: String,
    val title: String,
    val content: String
)
