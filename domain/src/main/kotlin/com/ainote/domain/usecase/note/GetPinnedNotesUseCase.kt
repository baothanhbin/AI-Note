package com.ainote.domain.usecase.note

import com.ainote.core.model.note.Note
import com.ainote.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPinnedNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return noteRepository.getPinnedNotes()
    }
}
