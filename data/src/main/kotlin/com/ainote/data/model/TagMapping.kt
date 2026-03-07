package com.ainote.data.model

import com.ainote.core.database.model.TagEntity
import com.ainote.core.model.note.Tag

fun TagEntity.asDomainModel(): Tag = Tag(
    id = id,
    name = name,
    color = color
)

fun Tag.asEntityModel(): TagEntity = TagEntity(
    id = id,
    name = name,
    color = color
)
