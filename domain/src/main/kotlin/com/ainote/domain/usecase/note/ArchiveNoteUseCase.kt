package com.ainote.domain.usecase.note

import com.ainote.domain.repository.NoteRepository
import javax.inject.Inject

class ArchiveNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(id: String, isArchived: Boolean) {
        noteRepository.archiveNote(id, isArchived)
    }
}
