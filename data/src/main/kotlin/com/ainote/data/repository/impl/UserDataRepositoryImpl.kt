package com.ainote.data.repository.impl

import com.ainote.core.datastore.PreferencesDataStore
import com.ainote.data.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) : UserDataRepository {

    override val useDarkMode: Flow<Boolean>
        get() = preferencesDataStore.userPreferencesFlow.map { it.useDarkMode }

    override val useMarkdownPreview: Flow<Boolean>
        get() = preferencesDataStore.userPreferencesFlow.map { it.useMarkdownPreview }

    override suspend fun setUseDarkMode(useDarkMode: Boolean) {
        preferencesDataStore.updateDarkMode(useDarkMode)
    }

    override suspend fun setUseMarkdownPreview(useMarkdownPreview: Boolean) {
        preferencesDataStore.updateMarkdownPreview(useMarkdownPreview)
    }
}
