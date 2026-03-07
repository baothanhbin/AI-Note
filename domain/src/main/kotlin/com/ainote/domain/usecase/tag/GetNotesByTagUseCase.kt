package com.ainote.domain.usecase.tag

import com.ainote.core.model.note.Note
import com.ainote.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesByTagUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(tagId: String): Flow<List<Note>> {
        return noteRepository.getNotesByTagId(tagId)
    }
}
