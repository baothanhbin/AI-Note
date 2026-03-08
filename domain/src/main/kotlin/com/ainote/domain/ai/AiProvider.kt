package com.ainote.domain.ai

interface AiProvider {
    suspend fun generateTitle(content: String): Result<String>
    suspend fun summarize(content: String): Result<String>
    suspend fun extractTags(content: String): Result<List<String>>
}
