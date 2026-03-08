package com.ainote.domain.usecase.tag

import com.ainote.core.model.note.Tag
import com.ainote.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTagsForNoteUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    operator fun invoke(noteId: String): Flow<List<Tag>> {
        return tagRepository.getTagsForNote(noteId)
    }
}
