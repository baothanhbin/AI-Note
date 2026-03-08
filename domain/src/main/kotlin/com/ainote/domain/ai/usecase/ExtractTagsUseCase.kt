package com.ainote.domain.ai.usecase

import com.ainote.domain.ai.AiProvider
import javax.inject.Inject

class ExtractTagsUseCase @Inject constructor(
    private val aiProvider: AiProvider
) {
    suspend operator fun invoke(content: String): Result<List<String>> {
        return aiProvider.extractTags(content)
    }
}
