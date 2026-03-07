package com.ainote.core.model.note

data class CodeBlock(
    val blockId: String,
    val noteId: String,
    val language: String,
    val content: String,
    val orderIndex: Int
)
