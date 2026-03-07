package com.ainote.data.repository

import com.ainote.core.model.note.Tag
import kotlinx.coroutines.flow.Flow

interface TagRepository {
    fun getAllTags(): Flow<List<Tag>>
    
    suspend fun getTagByName(name: String): Tag?
    
    fun getTagsForNote(noteId: String): Flow<List<Tag>>
    
    suspend fun saveTag(tag: Tag)
    
    suspend fun deleteTag(tag: Tag)
    
    suspend fun addTagToNote(noteId: String, tagId: String)
    
    suspend fun removeTagFromNote(noteId: String, tagId: String)
}
