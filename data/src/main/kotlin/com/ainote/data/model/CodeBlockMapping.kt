package com.ainote.data.model

import com.ainote.core.database.model.CodeBlockEntity
import com.ainote.core.model.note.CodeBlock

fun CodeBlockEntity.asDomainModel() = CodeBlock(
    blockId = blockId,
    noteId = noteId,
    language = language,
    content = content,
    orderIndex = orderIndex
)

fun CodeBlock.asEntityModel() = CodeBlockEntity(
    blockId = blockId,
    noteId = noteId,
    language = language,
    content = content,
    orderIndex = orderIndex
)
