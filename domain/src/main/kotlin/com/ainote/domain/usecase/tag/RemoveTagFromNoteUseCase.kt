package com.ainote.domain.usecase.tag

import com.ainote.domain.repository.TagRepository
import javax.inject.Inject

class RemoveTagFromNoteUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    suspend operator fun invoke(noteId: String, tagId: String) {
        tagRepository.removeTagFromNote(noteId, tagId)
    }
}
