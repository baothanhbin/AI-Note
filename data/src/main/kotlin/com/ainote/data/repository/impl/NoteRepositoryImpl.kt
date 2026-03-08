package com.ainote.data.repository.impl

import com.ainote.core.database.dao.NoteDao
import com.ainote.data.model.asDomainModel
import com.ainote.data.model.asEntityModel
import com.ainote.domain.repository.NoteRepository
import com.ainote.core.model.note.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities -> 
            entities.map { it.asDomainModel() } 
        }
    }

    override fun getPinnedNotes(): Flow<List<Note>> {
        return noteDao.getPinnedNotes().map { entities -> 
            entities.map { it.asDomainModel() } 
        }
    }

    override fun getNoteById(id: String): Flow<Note?> {
        return noteDao.getNoteById(id).map { entity -> 
            entity?.asDomainModel() 
        }
    }

    override suspend fun getNoteByTitle(title: String): Note? {
        return noteDao.getNoteByTitle(title)?.asDomainModel()
    }

    override suspend fun saveNote(note: Note) {
        noteDao.insertOrUpdateNote(note.asEntityModel())
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.asEntityModel())
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.asEntityModel())
    }

    override suspend fun archiveNote(id: String, isArchived: Boolean) {
        noteDao.updateArchiveStatus(id, isArchived)
    }

    override fun getNotesByTagId(tagId: String): Flow<List<Note>> {
        return noteDao.getNotesByTagId(tagId).map { entities ->
            entities.map { it.asDomainModel() }
        }
    }
}
