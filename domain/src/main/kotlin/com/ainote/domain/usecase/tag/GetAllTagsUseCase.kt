package com.ainote.domain.usecase.tag

import com.ainote.core.model.note.Tag
import com.ainote.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTagsUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    operator fun invoke(): Flow<List<Tag>> {
        return tagRepository.getAllTags()
    }
}
