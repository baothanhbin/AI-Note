package com.ainote.core.database.di

import android.content.Context
import androidx.room.Room
import com.ainote.core.database.AppDatabase
import com.ainote.core.database.dao.LinkDao
import com.ainote.core.database.dao.NoteDao
import com.ainote.core.database.dao.SearchDao
import com.ainote.core.database.dao.TagDao
import com.ainote.core.database.dao.ChecklistItemDao
import com.ainote.core.database.dao.CodeBlockDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ainote_database"
        ).build()
    }

    @Provides
    fun provideNoteDao(database: AppDatabase): NoteDao = database.noteDao()

    @Provides
    fun provideTagDao(database: AppDatabase): TagDao = database.tagDao()

    @Provides
    fun provideLinkDao(database: AppDatabase): LinkDao = database.linkDao()

    @Provides
    fun provideSearchDao(database: AppDatabase): SearchDao = database.searchDao()

    @Provides
    fun provideChecklistItemDao(database: AppDatabase): ChecklistItemDao = database.checklistItemDao()

    @Provides
    fun provideCodeBlockDao(database: AppDatabase): CodeBlockDao = database.codeBlockDao()
}
