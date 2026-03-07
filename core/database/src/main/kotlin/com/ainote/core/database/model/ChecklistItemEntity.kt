package com.ainote.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_items")
data class ChecklistItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "item_id")
    val itemId: String,
    @ColumnInfo(name = "note_id")
    val noteId: String,
    val content: String,
    @ColumnInfo(name = "is_checked")
    val isChecked: Boolean,
    @ColumnInfo(name = "order_index")
    val orderIndex: Int
)
