package com.ainote.data.repository.impl

import com.ainote.core.database.dao.TagDao
import com.ainote.core.database.model.NoteTagCrossRef
import com.ainote.data.model.asDomainModel
import com.ainote.data.model.asEntityModel
import com.ainote.data.repository.TagRepository
import com.ainote.core.model.note.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val tagDao: TagDao
) : TagRepository {

    override fun getAllTags(): Flow<List<Tag>> {
        return tagDao.getAllTags().map { entities ->
            entities.map { it.asDomainModel() }
        }
    }

    override fun getTagsForNote(noteId: String): Flow<List<Tag>> {
        return tagDao.getTagsForNote(noteId).map { entities ->
            entities.map { it.asDomainModel() }
        }
    }

    override suspend fun saveTag(tag: Tag) {
        tagDao.insertTag(tag.asEntityModel())
    }

    override suspend fun deleteTag(tag: Tag) {
        tagDao.deleteTag(tag.asEntityModel())
    }

    override suspend fun addTagToNote(noteId: String, tagId: String) {
        tagDao.insertNoteTagCrossRef(NoteTagCrossRef(noteId, tagId))
    }

    override suspend fun removeTagFromNote(noteId: String, tagId: String) {
        tagDao.deleteNoteTagCrossRef(NoteTagCrossRef(noteId, tagId))
    }
}
