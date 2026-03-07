package com.ainote.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "note_tags",
    primaryKeys = ["note_id", "tag_id"],
    indices = [
        Index("note_id"),
        Index("tag_id")
    ]
)
data class NoteTagCrossRef(
    @ColumnInfo(name = "note_id")
    val noteId: String,
    @ColumnInfo(name = "tag_id")
    val tagId: String
)
