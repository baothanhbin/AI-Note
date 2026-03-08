package com.ainote.domain.usecase.link

import com.ainote.core.model.note.NoteLink
import com.ainote.domain.repository.LinkRepository
import com.ainote.domain.repository.NoteRepository
import java.time.LocalDateTime
import javax.inject.Inject

class ProcessNoteLinksUseCase @Inject constructor(
    private val linkRepository: LinkRepository,
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(noteId: String, content: String) {
        // Find all outgoing links matching [[Title]]
        val regex = Regex("\\[\\[(.*?)\\]\\]")
        val matches = regex.findAll(content)
        val linkedTitles = matches.map { it.groupValues[1] }.toSet()

        // 1. Delete old outgoing links for this note
        linkRepository.deleteOutgoingLinks(noteId)

        // 2. Insert new outgoing links
        val now = LocalDateTime.now()
        for (title in linkedTitles) {
            if (title.isBlank()) continue
            
            // Look up note by title
            val targetNote = noteRepository.getNoteByTitle(title)
            if (targetNote != null) {
                // Avoid self-linking
                if (targetNote.id != noteId) {
                    val newLink = NoteLink(
                        fromNoteId = noteId,
                        toNoteId = targetNote.id,
                        createdAt = now
                    )
                    linkRepository.insertLink(newLink)
                }
            }
        }
    }
}
