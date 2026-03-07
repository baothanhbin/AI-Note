package com.ainote.domain.usecase.tag

import com.ainote.core.model.note.Tag
import com.ainote.data.repository.TagRepository
import java.util.UUID
import javax.inject.Inject

class AddTagToNoteUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    suspend operator fun invoke(noteId: String, tagName: String) {
        val normalizedName = tagName.trim()
        if (normalizedName.isBlank()) return

        var tag = tagRepository.getTagByName(normalizedName)
        if (tag == null) {
            tag = Tag(
                id = UUID.randomUUID().toString(),
                name = normalizedName
            )
            tagRepository.saveTag(tag)
        }

        tagRepository.addTagToNote(noteId, tag.id)
    }
}
