package com.ainote.core.model.graph

data class GraphNode(
    val noteId: String,
    val title: String,
    val degree: Int,
    val x: Float = 0f,
    val y: Float = 0f
)
