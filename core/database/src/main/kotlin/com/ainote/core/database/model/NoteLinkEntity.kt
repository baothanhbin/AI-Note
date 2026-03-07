package com.ainote.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "note_links",
    primaryKeys = ["from_note_id", "to_note_id"],
    indices = [
        Index("from_note_id"),
        Index("to_note_id")
    ]
)
data class NoteLinkEntity(
    @ColumnInfo(name = "from_note_id")
    val fromNoteId: String,
    @ColumnInfo(name = "to_note_id")
    val toNoteId: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)
