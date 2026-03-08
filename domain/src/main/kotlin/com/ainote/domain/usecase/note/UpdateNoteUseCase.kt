package com.ainote.domain.usecase.note

import com.ainote.core.model.note.Note
import com.ainote.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        noteRepository.updateNote(note)
    }
}
