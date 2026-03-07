package com.ainote.core.model.search

import com.ainote.core.model.note.Tag

data class SearchResult(
    val noteId: String,
    val title: String,
    val snippet: String,
    val score: Float,
    val matchedTags: List<Tag>? = null
)
