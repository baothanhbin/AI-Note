package com.ainote.domain.usecase.link

import com.ainote.core.model.note.Note
import com.ainote.domain.repository.LinkRepository
import com.ainote.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull

class GetOutgoingLinksUseCase @Inject constructor(
    private val linkRepository: LinkRepository,
    private val noteRepository: NoteRepository
) {
    operator fun invoke(noteId: String): Flow<List<Note>> {
        return linkRepository.getOutgoingLinks(noteId).map { links ->
            links.mapNotNull { link ->
                noteRepository.getNoteById(link.toNoteId).firstOrNull()
            }
        }
    }
}
