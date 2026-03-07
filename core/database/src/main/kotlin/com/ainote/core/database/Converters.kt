package com.ainote.core.database

import androidx.room.TypeConverter
import com.ainote.core.model.note.NoteType

class Converters {
    @TypeConverter
    fun fromNoteType(value: NoteType): String {
        return value.name
    }

    @TypeConverter
    fun toNoteType(value: String): NoteType {
        return try {
            NoteType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            NoteType.TEXT
        }
    }
}
