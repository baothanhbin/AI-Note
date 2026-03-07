package com.ainote.data.repository

import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val useDarkMode: Flow<Boolean>
    val useMarkdownPreview: Flow<Boolean>
    
    suspend fun setUseDarkMode(useDarkMode: Boolean)
    suspend fun setUseMarkdownPreview(useMarkdownPreview: Boolean)
}
