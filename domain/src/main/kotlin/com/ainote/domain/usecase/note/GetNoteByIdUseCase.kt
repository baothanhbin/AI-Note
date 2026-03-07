package com.ainote.domain.usecase.note

import com.ainote.core.model.note.Note
import com.ainote.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(id: String): Flow<Note?> {
        return noteRepository.getNoteById(id)
    }
}
