package com.ainote.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ainote.core.database.dao.LinkDao
import com.ainote.core.database.dao.NoteDao
import com.ainote.core.database.dao.SearchDao
import com.ainote.core.database.dao.TagDao
import com.ainote.core.database.model.ChecklistItemEntity
import com.ainote.core.database.model.CodeBlockEntity
import com.ainote.core.database.model.NoteEntity
import com.ainote.core.database.model.NoteFtsEntity
import com.ainote.core.database.model.NoteLinkEntity
import com.ainote.core.database.model.NoteTagCrossRef
import com.ainote.core.database.model.TagEntity

@Database(
    entities = [
        NoteEntity::class,
        TagEntity::class,
        NoteTagCrossRef::class,
        NoteLinkEntity::class,
        ChecklistItemEntity::class,
        CodeBlockEntity::class,
        NoteFtsEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun tagDao(): TagDao
    abstract fun linkDao(): LinkDao
    abstract fun searchDao(): SearchDao
    abstract fun checklistItemDao(): com.ainote.core.database.dao.ChecklistItemDao
    abstract fun codeBlockDao(): com.ainote.core.database.dao.CodeBlockDao
}
