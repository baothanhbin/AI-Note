package com.ainote.data.ai

import com.ainote.data.ai.provider.MockAiProvider
import com.ainote.domain.ai.AiProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AiModule {
    
    // For Phase 2 we use MockAiProvider. 
    // Later we can swap this with GeminiAiProvider
    @Binds
    abstract fun bindAiProvider(
        mockProvider: MockAiProvider
    ): AiProvider
}
