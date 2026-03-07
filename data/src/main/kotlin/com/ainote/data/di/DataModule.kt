package com.ainote.data.di

import com.ainote.data.repository.NoteRepository
import com.ainote.data.repository.UserDataRepository
import com.ainote.data.repository.impl.NoteRepositoryImpl
import com.ainote.data.repository.impl.UserDataRepositoryImpl
import com.ainote.data.repository.TagRepository
import com.ainote.data.repository.impl.TagRepositoryImpl
import com.ainote.data.repository.SearchRepository
import com.ainote.data.repository.impl.SearchRepositoryImpl
import com.ainote.data.repository.LinkRepository
import com.ainote.data.repository.impl.LinkRepositoryImpl
import com.ainote.data.repository.ChecklistItemRepository
import com.ainote.data.repository.impl.ChecklistItemRepositoryImpl
import com.ainote.data.repository.CodeBlockRepository
import com.ainote.data.repository.impl.CodeBlockRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindNoteRepository(
        noteRepositoryImpl: NoteRepositoryImpl
    ): NoteRepository

    @Binds
    abstract fun bindUserDataRepository(
        userDataRepositoryImpl: UserDataRepositoryImpl
    ): UserDataRepository

    @Binds
    abstract fun bindTagRepository(
        tagRepositoryImpl: TagRepositoryImpl
    ): TagRepository

    @Binds
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    abstract fun bindLinkRepository(
        linkRepositoryImpl: LinkRepositoryImpl
    ): LinkRepository

    @Binds
    abstract fun bindChecklistItemRepository(
        checklistItemRepositoryImpl: ChecklistItemRepositoryImpl
    ): ChecklistItemRepository

    @Binds
    abstract fun bindCodeBlockRepository(
        codeBlockRepositoryImpl: CodeBlockRepositoryImpl
    ): CodeBlockRepository
}
