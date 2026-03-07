package com.ainote.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "code_blocks")
data class CodeBlockEntity(
    @PrimaryKey
    @ColumnInfo(name = "block_id")
    val blockId: String,
    @ColumnInfo(name = "note_id")
    val noteId: String,
    val language: String,
    val content: String,
    @ColumnInfo(name = "order_index")
    val orderIndex: Int
)
