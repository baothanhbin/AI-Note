package com.ainote.domain.usecase.graph

import com.ainote.core.model.graph.GraphData
import com.ainote.core.model.graph.GraphEdge
import com.ainote.core.model.graph.GraphNode
import com.ainote.domain.repository.LinkRepository
import com.ainote.domain.usecase.note.GetAllNotesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetGraphDataUseCase @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val linkRepository: LinkRepository
) {
    operator fun invoke(): Flow<GraphData> {
        return combine(
            getAllNotesUseCase(),
            linkRepository.getAllLinks()
        ) { notes, links ->
            val nodes = notes.map { note ->
                GraphNode(
                    noteId = note.id,
                    title = note.title.ifBlank { "Untitled" },
                    degree = 0, // Degree computation might be handled separately later
                    x = 0f,
                    y = 0f
                )
            }
            
            val edges = links.map { link ->
                GraphEdge(
                    fromNoteId = link.fromNoteId,
                    toNoteId = link.toNoteId
                )
            }
            
            GraphData(nodes = nodes, edges = edges)
        }
    }
}
