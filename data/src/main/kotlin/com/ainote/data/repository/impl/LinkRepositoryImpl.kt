package com.ainote.data.repository.impl

import com.ainote.core.database.dao.LinkDao
import com.ainote.core.database.model.NoteLinkEntity
import com.ainote.core.model.note.NoteLink
import com.ainote.data.repository.LinkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

class LinkRepositoryImpl @Inject constructor(
    private val linkDao: LinkDao
) : LinkRepository {
    override suspend fun insertLink(link: NoteLink) {
        linkDao.insertLink(link.toEntity())
    }

    override suspend fun deleteLink(link: NoteLink) {
        linkDao.deleteLink(link.toEntity())
    }

    override fun getOutgoingLinks(noteId: String): Flow<List<NoteLink>> {
        return linkDao.getOutgoingLinks(noteId).map { docs -> docs.map { it.toExternalModel() } }
    }

    override fun getBacklinks(noteId: String): Flow<List<NoteLink>> {
        return linkDao.getBacklinks(noteId).map { docs -> docs.map { it.toExternalModel() } }
    }

    override fun getAllLinks(): Flow<List<NoteLink>> {
        return linkDao.getAllLinks().map { docs -> docs.map { it.toExternalModel() } }
    }
}

fun NoteLink.toEntity() = NoteLinkEntity(
    fromNoteId = fromNoteId,
    toNoteId = toNoteId,
    createdAt = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
)

fun NoteLinkEntity.toExternalModel() = NoteLink(
    fromNoteId = fromNoteId,
    toNoteId = toNoteId,
    createdAt = Instant.ofEpochMilli(createdAt).atZone(ZoneId.systemDefault()).toLocalDateTime()
)
