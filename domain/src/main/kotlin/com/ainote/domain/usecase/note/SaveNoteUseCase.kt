package com.ainote.domain.usecase.note

import com.ainote.core.model.note.Note
import com.ainote.data.repository.NoteRepository
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        noteRepository.saveNote(note)
    }
}
