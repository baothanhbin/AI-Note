package com.ainote.core.model.note

data class CodeBlock(
    val id: String,
    val noteId: String,
    val language: String,
    val content: String,
    val orderIndex: Int
)
