package com.ainote.data.ai.provider

import com.ainote.domain.ai.AiProvider
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockAiProvider @Inject constructor() : AiProvider {
    override suspend fun generateTitle(content: String): Result<String> {
        delay(1000) // Simulate network delay
        if (content.isBlank()) return Result.failure(Exception("Content is empty"))
        
        // Very basic mock logic
        val words = content.split(" ")
        val suggestedTitle = if (words.size > 5) {
            words.take(5).joinToString(" ") + "..."
        } else {
            content
        }.replaceFirstChar { it.uppercase() }
        
        return Result.success(suggestedTitle)
    }

    override suspend fun summarize(content: String): Result<String> {
        delay(1500)
        if (content.isBlank()) return Result.failure(Exception("Content is empty"))
        
        return Result.success("This is an AI summary of your text. It contains the key points and highlights. (Mocked)")
    }

    override suspend fun extractTags(content: String): Result<List<String>> {
        delay(800)
        if (content.isBlank()) return Result.failure(Exception("Content is empty"))
        
        // Mock tagging logic
        val mockTags = buildList {
            add("Idea")
            if (content.contains("android", ignoreCase = true)) add("Android")
            if (content.contains("code", ignoreCase = true)) add("Development")
            if (content.contains("plan", ignoreCase = true)) add("Planning")
        }
        
        return Result.success(mockTags)
    }
}
