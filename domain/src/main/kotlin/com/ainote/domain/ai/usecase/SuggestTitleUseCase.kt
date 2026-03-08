package com.ainote.domain.ai.usecase

import com.ainote.domain.ai.AiProvider
import javax.inject.Inject

class SuggestTitleUseCase @Inject constructor(
    private val aiProvider: AiProvider
) {
    suspend operator fun invoke(content: String): Result<String> {
        return aiProvider.generateTitle(content)
    }
}
